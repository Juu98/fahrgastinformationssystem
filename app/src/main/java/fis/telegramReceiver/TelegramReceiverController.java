package fis.telegramReceiver;

import fis.common.ConfigurationException;
import fis.telegrams.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * empfängt TCP Telegramme mit Zugdaten. Steuert Kontrollfluss:
 * Verbindungsaufbau, Registrierung und eventuelles Senden von
 * Statustelegrammen, stößt das Parsen der byte-Arrays zu Telegrammen an, sendet
 * Telegrammevents.
 *
 * @author spiollinux
 */

@Service
public class TelegramReceiverController extends Thread implements ApplicationEventPublisherAware, SmartLifecycle {

	private static final Logger LOGGER = Logger.getLogger(TelegramReceiverController.class);
	private final TelegramReceiverConfig receiverConfig;
	private final TelegramReceiver receiver;
	private final Socket server;
	private final TelegramParser parser;
	private List<byte[]> telegramRawQueue;
	private ConnectionStatus connectionStatus;
	// needed for events
	private ApplicationEventPublisher publisher;

	/**
	 * setzt alle nötigen Kollaborateure der Klasse, initialisiert Objekt
	 * normalerweise durch Spring @Autowired
	 *
	 * @param config   eine TelegramReceiverConfig mit Verbindungsdaten und timeouts
	 * @param server   ein Socket zur Verbindung mit dem Server
	 * @param receiver ein TelegramReceiver
	 * @param parser   ein TelegramParser
	 */
	@Autowired
	public TelegramReceiverController(TelegramReceiverConfig config, Socket server, TelegramReceiver receiver,
	                                  TelegramParser parser) {
		Assert.notNull(config, "TelegramReceiverConfig mustn't be null");
		Assert.notNull(server, "Socket mustn't be null");
		Assert.notNull(receiver, "TelegramReceiver mustn't be null");
		Assert.notNull(parser, "TelegramParser mustn't be null");
		this.receiverConfig = config;
		this.server = server;
		this.receiver = receiver;
		this.parser = parser;
		this.connectionStatus = ConnectionStatus.OFFLINE;
	}

	/**
	 * entspricht Thread.start, setzt zusätzlich den Thread als daemon (kann
	 * also jederzeit abgeschossen werden) wird wegen {@link SmartLifecycle
	 * automatisch beim Start der SpringApplication aufgerufen}
	 */
	@Override
	public void start() {
		setDaemon(true);
		super.start();
	}

	/**
	 * Teil von {@link SmartLifecycle} Gibt zurück, ob der Thread noch läuft
	 *
	 * @return Status des Threads (true - Thread läuft, false - Thread läuft
	 * nicht)
	 */
	@Override
	public boolean isRunning() {
		return this.isAlive();
	}

	/**
	 * Eventloop des TelegramReceiverControllers. Schleife, solange der Thread
	 * nicht interrupted ist. Versucht bei nicht bestehender Verbindung immer,
	 * Verbindung herzustellen und sich zu registrieren. Bei
	 * ConnectionStatus.ONLINE: Steuerung des Datenflusses der Telegramm-byte[]
	 * vom Socket zum Parser, Senden der Telegrammevents
	 */
	@Override
	public void run() {
		LOGGER.info("TelegramReceiver started");
		while (!currentThread().isInterrupted()) {
			// try to connect until there is a connection
			while (this.getConnectionStatus() == ConnectionStatus.OFFLINE) {
				try {
					connectToHost();
					//if connection fails, exception is thrown and code beneath never executed
					try {
						register();
						break;
					} catch (IOException e) {
						LOGGER.error("error while sending RegistrationTelegram: " + e.getMessage());
						LOGGER.debug("", e);
						this.setConnectionStatus(ConnectionStatus.OFFLINE);
					}
				} catch (ConfigurationException e) {
					this.setConnectionStatus(ConnectionStatus.OFFLINE);
					LOGGER.error("configuration error: " + e.getMessage());
					LOGGER.debug("", e);
				} catch (IOException e) {
					this.setConnectionStatus(ConnectionStatus.OFFLINE);
					LOGGER.error("connecting to server failed: " + e.getMessage());
					LOGGER.debug("", e);
				}

				try {
					Thread.sleep(receiverConfig.getTimeTillReconnect());
				} catch (InterruptedException e) {
					this.interrupt();
					break;
				}
			}
			// create list for temporary storage of rawTelegram byte[]
			this.telegramRawQueue = new LinkedList<>();
			// handling the connected state
			LOGGER.debug("handling, " + getConnectionStatus());
			Future<byte[]> currentTelegram = null;
			try {
				while (getConnectionStatus() != ConnectionStatus.OFFLINE && !Thread.currentThread().isInterrupted()) {
					LOGGER.debug("Creating Future");
					currentTelegram = receiver.parseConnection(server.getInputStream());
					LOGGER.debug("Future created");
					do {
						if (!telegramRawQueue.isEmpty()) {
							byte[] currentRawTele = telegramRawQueue.get(0);
							LOGGER.debug(Arrays.toString(currentRawTele));
							// ignore nullbyte telegrams
							if (Arrays.equals(currentRawTele, new byte[255])) {
								telegramRawQueue.remove(0);
								continue;
							}

							try {
								Telegram telegramResponse = parser.parse(currentRawTele);
								LOGGER.debug("Parsed " + telegramResponse);
								if (this.getConnectionStatus() == ConnectionStatus.CONNECTING)
									setConnectionStatus(ConnectionStatus.INIT);

								if (getConnectionStatus() == ConnectionStatus.INIT
										&& telegramResponse.getClass() == TrainRouteEndTelegram.class) {
									setConnectionStatus(ConnectionStatus.ONLINE);
									receiver.sendTelegram(server.getOutputStream(),
											new ClientStatusTelegram("FIS", (byte) 0x00));
								}
								if (telegramResponse.getClass() != TrainRouteEndTelegram.class) {
									publisher.publishEvent(new TelegramParsedEvent(telegramResponse));
								}
							} catch (TelegramParseException e) {
								LOGGER.error("error while parsing telegram: " + e.getMessage());
								LOGGER.debug("", e);
							}
							telegramRawQueue.remove(0);
						}
						// tell scheduler to reschedule
						else
							Thread.yield();
					} while (!currentTelegram.isDone() && !Thread.currentThread().isInterrupted());
					telegramRawQueue.add(currentTelegram.get());
				}
			} catch (IOException e) { // Error Handling
				LOGGER.error("Socket error: " + e.getMessage());
				LOGGER.debug("", e);
			} catch (InterruptedException e) {
				LOGGER.debug("Future interrupted");
				this.interrupt();
			} catch (ExecutionException e) {
				LOGGER.error("receiving telegram bytes failed, cause: " + e.getCause());
				LOGGER.debug("", e);
			} finally {
				try {
					server.close();
				} catch (IOException e) {
					LOGGER.error("closing the Socket failed: " + e.getMessage());
					LOGGER.debug("", e);
				}
				setConnectionStatus(ConnectionStatus.OFFLINE);
			}
			// cancel Future listening for telegramserver bytes
			if (currentTelegram != null)
				if (!currentTelegram.isDone()) {
					currentTelegram.cancel(true);
				}
		}
		// safely stopping the Thread
		if (!server.isClosed()) {
			try {
				server.close(); // should also cause the parseConnection threads
				// to stop (SocketException)
			} catch (IOException e) {
				LOGGER.error("closing the Socket failed: " + e.getMessage());
				LOGGER.debug("", e);
			}
			setConnectionStatus(ConnectionStatus.OFFLINE);
		}
	}

	/**
	 * sendet ein Anmeldetelegramm an den Telegrammserver
	 *
	 * @throws IOException
	 */
	private void register() throws IOException {
		SendableTelegram regTelegram = new RegistrationTelegram(receiverConfig.getClientID());
		LOGGER.info("Registering to the telegram server");
		LOGGER.debug(regTelegram);
		receiver.sendTelegram(server.getOutputStream(), regTelegram);
	}

	/**
	 * verbindet den Socket server mit dem Telegrammserver unter Verwendung der
	 * Daten der Konfigurationsdatei
	 *
	 * @throws IOException
	 * @throws ConfigurationException bei ungültiger Verbindungskonfiguration
	 */
	public void connectToHost() throws IOException, ConfigurationException {
		setConnectionStatus(ConnectionStatus.CONNECTING);
		try {
			SocketAddress hostAddress = new InetSocketAddress(receiverConfig.getHostname(), receiverConfig.getPort());
			try {
				server.connect(hostAddress, receiverConfig.getTimeout());
				LOGGER.info("Connected to " + receiverConfig.getHostname() + ":" + receiverConfig.getPort());
			} catch (IllegalArgumentException e) {
				throw (new ConfigurationException("Telegramserver: configuration of timeout not valid"));
			}
		} catch (IllegalArgumentException e) {
			throw (new ConfigurationException("Telegramserver: configuration of hostname or port not valid"));
		}
	}

	/**
	 * gibt den ConnectionStatus dieses TelegramReceiverControllers
	 * thread-safety: sollte "safe enough" sein, es könnte höchstens passieren,
	 * dass der connectionStatus nach dem check auf null gesetzt wird. Der
	 * connectionStatus wird allerdings nie auf null gesetzt -> kein Locking
	 * notwendig.
	 *
	 * @return ConnectionStatus
	 * @throws NullPointerException
	 */
	public ConnectionStatus getConnectionStatus() throws NullPointerException {
		if (this.connectionStatus == null)
			throw (new NullPointerException("connectionStatus is null"));
		return this.connectionStatus;
	}

	/**
	 * setzt den connectionStatus dieses TelegramReceiverControllers
	 * thread-safety: ja, Schreiboperationen werden synchronisiert
	 *
	 * @param connectionStatus
	 */
	public synchronized void setConnectionStatus(ConnectionStatus connectionStatus) {
		if (connectionStatus == null)
			throw (new IllegalArgumentException("connectionStatus mustn't be null"));
		this.connectionStatus = connectionStatus;
		publisher.publishEvent(new ConnectionStatusEvent(connectionStatus));
	}

	/**
	 * notwendig für @link{ApplicationEventPublisherAware}. Wird üblicherweise
	 * automatisch von Spring aufgerufen
	 *
	 * @param applicationEventPublisher
	 */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

	/**
	 * sorgt für automatischen Start des TelegramReceivers Teil von
	 * {@link SmartLifecycle}
	 *
	 * @return automatisch gestartet?
	 */
	@Override
	public boolean isAutoStartup() {
		return true;
	}

	/**
	 * Methode, mit der ein {@link SmartLifecycle} von Spring gestoppt wird
	 *
	 * @param callback
	 */
	@Override
	public void stop(Runnable callback) {
		this.interrupt();
		while (this.isRunning()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				this.interrupt();
			}
		}
		callback.run();
	}

	/**
	 * bestimmt, wann die Komponente gestartet wird: Je höher die Zahl, desto
	 * später. Stoppen in umgekehrter Reihenfolge
	 *
	 * @return Nummer der Startphase
	 */
	@Override
	public int getPhase() {
		return 5;
	}

}
