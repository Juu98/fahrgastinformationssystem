package fis.telegrams;

import fis.TimeTableController;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TelegramReceiverController extends Thread {

    private final TelegramReceiverConfig receiverConfig;
    private final TelegramReceiver receiver;
    private final Socket server;
    private ConnectionStatus connectionStatus;
    private TimeTableController timeTableController;

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
			    try {
				    connectToHost();
			    } catch (IOException e) {
				    this.setConnectionStatus(ConnectionStatus.OFFLINE);
				    //TODO: Log connection fail
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
                    receiver.handleConnection(server.getInputStream());
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

    public void connectToHost() throws IOException {
        setConnectionStatus(ConnectionStatus.CONNECTING);
        SocketAddress hostAddress = new InetSocketAddress(receiverConfig.getHostname(), receiverConfig.getPort());
        server.connect(hostAddress, receiverConfig.getTimeout());
        setConnectionStatus(ConnectionStatus.ONLINE);
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
}
