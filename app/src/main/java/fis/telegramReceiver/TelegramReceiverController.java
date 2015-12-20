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
 * Created by spiollinux on 07.11.15.
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
	private boolean running;
	//needed for events
	private ApplicationEventPublisher publisher;

	@Autowired
	public TelegramReceiverController(TelegramReceiverConfig config, Socket server, TelegramReceiver receiver, TelegramParser parser) {
		Assert.notNull(config,"TelegramReceiverConfig mustn't be null");
		Assert.notNull(server,"Socket mustn't be null");
		Assert.notNull(receiver,"TelegramReceiver mustn't be null");
		Assert.notNull(parser,"TelegramParser mustn't be null");
		this.receiverConfig = config;
		this.server = server;
		this.receiver = receiver;
		this.parser = parser;
		this.connectionStatus = ConnectionStatus.OFFLINE;
	}

	@Override
	public void start() {
		setDaemon(true);
		super.start();
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}

	@Override
	public void run() {
		this.running = true;
		LOGGER.info("TelegramReceiver started");
		while(!currentThread().isInterrupted()) {
			//try to connect until there is a connection
			while (this.getConnectionStatus() == ConnectionStatus.OFFLINE) {
				try {
					connectToHost();
				} catch (ConfigurationException e) {
					this.setConnectionStatus(ConnectionStatus.OFFLINE);
					LOGGER.error("configuration error: " + e.getMessage());
					LOGGER.debug("",e);
				} catch (IOException e) {
					this.setConnectionStatus(ConnectionStatus.OFFLINE);
					LOGGER.error("connecting to server failed: " + e.getMessage());
					LOGGER.debug("",e);
				}
				if(server.isConnected()) {
					try {
						register();
						break;
					} catch (IOException e) {
						LOGGER.error("error while sending RegistrationTelegram: " + e.getMessage());
						LOGGER.debug("",e);
						this.setConnectionStatus(ConnectionStatus.OFFLINE);
					}
				}
				try {
					Thread.sleep(receiverConfig.getTimeTillReconnect());
				} catch (InterruptedException e) {
					this.interrupt();
					break;
				}
			}
			//create list for temporary storage of rawTelegram byte[]
			this.telegramRawQueue = new LinkedList<>();
			// handling the connected state
			LOGGER.debug("handling, " + getConnectionStatus());
			try {
				while (getConnectionStatus() != ConnectionStatus.OFFLINE && !Thread.currentThread().isInterrupted()) {
					LOGGER.debug("Creating Future");
					Future<byte[]> currentTelegram = receiver.parseConnection(server.getInputStream());
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

								if (getConnectionStatus() == ConnectionStatus.CONNECTING
										&& telegramResponse.getClass() == TrainRouteEndTelegram.class) {
									setConnectionStatus(ConnectionStatus.ONLINE);
									receiver.sendTelegram(server.getOutputStream(), new ClientStatusTelegram("FIS", (byte) 0x00));
								}
								if (telegramResponse.getClass() != TrainRouteEndTelegram.class){
									publisher.publishEvent(new TelegramParsedEvent(telegramResponse));
								}
							} catch (TelegramParseException e) {
								LOGGER.error("error while parsing telegram: " + e.getMessage());
								LOGGER.debug("", e);
							}
							telegramRawQueue.remove(0);
						}
						//tell scheduler to reschedule
						else
							Thread.yield();
					} while (!currentTelegram.isDone() && !Thread.currentThread().isInterrupted());
					telegramRawQueue.add(currentTelegram.get());
				}
			} catch (IOException e) { //Error Handling
				LOGGER.error("Socket error: " + e.getMessage());
				LOGGER.debug("", e);
			} catch (InterruptedException e) {
				this.interrupt();
			} catch (ExecutionException e) {
				LOGGER.error("receiving telegram bytes failed, cause: " + e.getCause());
				LOGGER.debug("", e);
			}
			finally {
				try {
					server.close();
				} catch (IOException e) {
					LOGGER.error("closing the Socket failed: " + e.getMessage());
					LOGGER.debug("", e);
				}
				setConnectionStatus(ConnectionStatus.OFFLINE);
			}
		}
		//safely stopping the Thread
		if(!server.isClosed()) {
			try {
				server.close(); //should also cause the parseConnection threads to stop (SocketException)
			} catch (IOException e) {
				LOGGER.error("closing the Socket failed: " + e.getMessage());
				LOGGER.debug("", e);
			}
			setConnectionStatus(ConnectionStatus.OFFLINE);
		}
		this.running = false;
	}

	private void register() throws IOException {
		SendableTelegram regTelegram = new RegistrationTelegram(receiverConfig.getClientID());
		LOGGER.info("Registering to the telegram server");
		LOGGER.debug(regTelegram);
		receiver.sendTelegram(server.getOutputStream(), regTelegram);
	}

	public void connectToHost() throws IOException, ConfigurationException {
		setConnectionStatus(ConnectionStatus.CONNECTING);
		try {
			SocketAddress hostAddress = new InetSocketAddress(receiverConfig.getHostname(), receiverConfig.getPort());
			try {
				server.connect(hostAddress, receiverConfig.getTimeout());
				LOGGER.info("Connected to " + receiverConfig.getHostname() + ":" + receiverConfig.getPort());
			} catch (IllegalArgumentException e) {
				throw(new ConfigurationException("Telegramserver: configuration of timeout not valid"));
			}
		} catch (IllegalArgumentException e) {
			throw(new ConfigurationException("Telegramserver: configuration of hostname or port not valid"));
		}
	}

	/**
	 * returns the ConnectionStatus of the parser
	 * thread-safety: should be "safe enough" as the worst thing that can happen is connectionStatus being set to null
	 * after check for null. But connectionStatus is never set to null -> no locking necessary.
	 * @return ConnectionStatus
	 * @throws NullPointerException
	 */
	public ConnectionStatus getConnectionStatus() throws NullPointerException{
		if (this.connectionStatus == null)
			throw(new NullPointerException("connectionStatus is null"));
		return this.connectionStatus;
	}

	/**
	 * sets the connectionStatus of the parser. Also publishes a ConnectionStatusEvent.
	 * thread-safety: yes, writes are synchronized
	 * @param connectionStatus
	 */
	public synchronized void setConnectionStatus(ConnectionStatus connectionStatus) {
		if (connectionStatus == null)
			throw(new IllegalArgumentException("connectionStatus mustn't be null"));
		this.connectionStatus = connectionStatus;
		publisher.publishEvent(new ConnectionStatusEvent(connectionStatus));
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		this.interrupt();
	}

	@Override
	public int getPhase() {
		return Integer.MAX_VALUE;
	}
}
