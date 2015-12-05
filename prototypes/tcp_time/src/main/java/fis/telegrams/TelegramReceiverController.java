package fis.telegrams;

import fis.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by spiollinux on 07.11.15.
 */
@Service
public class TelegramReceiverController extends Thread implements ApplicationEventPublisherAware{

    private final TelegramReceiverConfig receiverConfig;
    private final TelegramReceiver receiver;
    private final Socket server;
    private ConnectionStatus connectionStatus;
	//needed for events
	private ApplicationEventPublisher publisher;

	@Autowired
    public TelegramReceiverController(TelegramReceiver receiver, TelegramReceiverConfig config, Socket server) {
		Assert.notNull(receiver,"TelegramReceiver mustn't be null");
		Assert.notNull(config,"TelegramReceiverConfig mustn't be null");
		Assert.notNull(server,"Socket mustn't be null");
		this.receiver = receiver;
		this.receiverConfig = config;
		this.server = server;
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
		    //Todo: reconnecting after connection loss
		    while (this.getConnectionStatus() != ConnectionStatus.ONLINE) {
			    if(this.getConnectionStatus() == ConnectionStatus.OFFLINE) {
				    try {
					    connectToHost();
				    } catch (IOException e) {
					    this.setConnectionStatus(ConnectionStatus.OFFLINE);
					    //TODO: Log connection fail
				    } catch (ConfigurationException e) {
					    //Todo: Log config error
				    }
			    }
			    if(server.isConnected()) {
					try {
						register();
					} catch (IOException e) {
						//TODO: Log connection fail
						this.setConnectionStatus(ConnectionStatus.OFFLINE);
					} catch (TelegramParseException e) {
						this.setConnectionStatus(ConnectionStatus.CONNECTING);
						//Todo: log parse error
					}
			    }
			    try {
				    Thread.sleep(receiverConfig.getTimeTillReconnect());
			    } catch (InterruptedException e) {
				    this.interrupt();
				    break;
			    }

		    }
		    try {
                while (getConnectionStatus() == ConnectionStatus.ONLINE && !Thread.currentThread().isInterrupted()) {
                    receiver.handleConnection(server.getInputStream(), server.getOutputStream());
                }
		    } catch (IOException e) {
			    //Todo: handle error
			    e.printStackTrace();
		    } finally {
			    try {
				    server.close();
				    setConnectionStatus(ConnectionStatus.OFFLINE);
			    } catch (IOException e) {
				    //Todo: handle error
				    e.printStackTrace();
			    }
		    }
	    }
	    //safely stopping the Thread
	    if(!server.isClosed()) {
		    try {
			    server.close(); //should also cause the parseConnection threads to stop (SocketException)
		    } catch (IOException e) {
			    //Todo: handle exception
			    e.printStackTrace();
		    }
		    setConnectionStatus(ConnectionStatus.OFFLINE);
	    }
    }

	private void register() throws IOException, TelegramParseException {
		RegistrationTelegram regTelegram = new RegistrationTelegram(receiverConfig.getClientID());
		Telegram responseTelegram = receiver.sendTelegram(server.getInputStream(), server.getOutputStream(), regTelegram);

		if(responseTelegram != null) {
			if(responseTelegram.getClass() == LabTimeTelegram.class) {
				setConnectionStatus(ConnectionStatus.ONLINE);
				//forward telegram
				publisher.publishEvent(new TelegramParsedEvent(responseTelegram));
				return;
			}
		}
		//Todo: Login error
	}

	public void connectToHost() throws IOException, ConfigurationException {
        setConnectionStatus(ConnectionStatus.CONNECTING);
	    try {
		    SocketAddress hostAddress = new InetSocketAddress(receiverConfig.getHostname(), receiverConfig.getPort());
		    try {
			    server.connect(hostAddress, receiverConfig.getTimeout());
		    } catch (IllegalArgumentException e) {
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
