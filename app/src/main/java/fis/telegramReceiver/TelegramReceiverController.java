package fis.telegramReceiver;

import fis.ConfigurationException;
import fis.telegrams.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by spiollinux on 07.11.15.
 */

@Service
public class TelegramReceiverController extends Thread implements ApplicationEventPublisherAware{

    private final TelegramReceiverConfig receiverConfig;
    private final TelegramReceiver receiver;
    private final Socket server;
	private final TelegramParser parser;
	private List<byte[]> telegramRawQueue;
    private ConnectionStatus connectionStatus;
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
    public void run() {
	    while(!currentThread().isInterrupted()) {
		    //try to connect until there is a connection
		    while (this.getConnectionStatus() == ConnectionStatus.OFFLINE) {
				try {
					connectToHost();
				} catch (IOException e) {
					this.setConnectionStatus(ConnectionStatus.OFFLINE);
					//TODO: Log connection fail
				} catch (ConfigurationException e) {
					this.setConnectionStatus(ConnectionStatus.OFFLINE);
					//Todo: Log config error
				}
			    if(server.isConnected()) {
					try {
						System.out.println("Vor register()");
						register();
						System.out.println("nach register()");
						break;
					}
					catch (IOException e) {
						//TODO: Log connection fail
						this.setConnectionStatus(ConnectionStatus.OFFLINE);
					}
			    }
			    try {
				    Thread.sleep(receiverConfig.getTimeTillReconnect());
			    }
			    catch (InterruptedException e) {
				    this.interrupt();
				    break;
			    }
		    }
		    //create list for temporary storage of rawTelegram byte[]
		    this.telegramRawQueue = new LinkedList<>();
		    // handling the connected state
		    System.err.println("handling");
		    try {
			    while (getConnectionStatus() != ConnectionStatus.OFFLINE && !Thread.currentThread().isInterrupted()) {
				    Future<byte[]> currentTelegram = receiver.parseConnection(server.getInputStream());
				    do {
					    if (!telegramRawQueue.isEmpty()) {
						    System.err.println("not empty");
						    try {
							    System.err.println("parsing");
							    Telegram telegramResponse = parser.parse(telegramRawQueue.get(0));
							    System.err.println(telegramResponse);

							    if (getConnectionStatus() == ConnectionStatus.CONNECTING
									    && telegramResponse.getClass() == TrainRouteEndTelegram.class) {
								    setConnectionStatus(ConnectionStatus.ONLINE);
								    System.out.println("Sending ClientStatus telegram");
								    receiver.sendTelegram(server.getOutputStream(), new ClientStatusTelegram("FIS", (byte) 0x00));
							    } else {
								    publisher.publishEvent(new TelegramParsedEvent(telegramResponse));
							    }
						    }
						    catch (TelegramParseException e) {
							    //Todo: Log parse exception
						    }
						    telegramRawQueue.remove(0);
					    }
					    //tell scheduler to reschedule
					    else
						    Thread.yield();
				    } while (!currentTelegram.isDone() && !Thread.currentThread().isInterrupted());
				    telegramRawQueue.add(currentTelegram.get());
			    }
		    }
		    //error handling
		    catch (IOException e) {
			    //Todo: handle error
			    e.printStackTrace();
		    }
		    catch (InterruptedException e) {
			    this.interrupt();
		    }
		    catch (ExecutionException e) {
			    e.printStackTrace();
		    }
		    finally {
			    try {
				    server.close();
				    setConnectionStatus(ConnectionStatus.OFFLINE);
			    }
			    catch (IOException e) {
				    //Todo: handle error
				    e.printStackTrace();
			    }
		    }
	    }
	    //safely stopping the Thread
	    if(!server.isClosed()) {
		    try {
			    server.close(); //should also cause the parseConnection threads to stop (SocketException)
		    }
		    catch (IOException e) {
			    //Todo: handle exception
			    e.printStackTrace();
		    }
		    setConnectionStatus(ConnectionStatus.OFFLINE);
	    }
    }

	private void register() throws IOException {
		SendableTelegram regTelegram = new RegistrationTelegram(receiverConfig.getClientID());
		System.out.println("Sending registration telegram");
		receiver.sendTelegram(server.getOutputStream(), regTelegram);
		//Todo: Login error
	}

	public void connectToHost() throws IOException, ConfigurationException {
        setConnectionStatus(ConnectionStatus.CONNECTING);
	    try {
		    SocketAddress hostAddress = new InetSocketAddress(receiverConfig.getHostname(), receiverConfig.getPort());
		    try {
			    server.connect(hostAddress, receiverConfig.getTimeout());
		    }
		    catch (IllegalArgumentException e) {
			    throw(new ConfigurationException("Telegramserver: configuration of timeout not valid"));
		    }
	    }
		catch (IllegalArgumentException e) {
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
     * sets the connectionStatus of the parser.
     * thread-safety: yes, writes are synchronized
     * @param connectionStatus
     */
    public synchronized void setConnectionStatus(ConnectionStatus connectionStatus) {
        if (connectionStatus == null)
            throw(new IllegalArgumentException("connectionStatus mustn't be null"));
        this.connectionStatus = connectionStatus;
    }

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}
}
