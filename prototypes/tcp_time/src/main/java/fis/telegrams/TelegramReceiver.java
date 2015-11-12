package fis.telegrams;

import fis.TimeTableController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
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
@ConfigurationProperties(prefix = "telegramserver") //setting hostname, port from application.yml
public class TelegramReceiver extends Thread {

    @Autowired
    TelegramReceiverConfig receiverConfig;
    private Socket server;
    private ConnectionStatus connectionStatus;
    private List<byte[]> telegramQueue;
    private TimeTableController timeTableController;

    public TelegramReceiver() {
        this.telegramQueue = new LinkedList<>();
        this.connectionStatus = ConnectionStatus.OFFLINE;
    }

    @Override
    public void start() {
        setDaemon(true);
        super.start();
    }

    @Override
    public void run() {
        //try to connect until there is a connection
        //Todo: reconnecting after connection loss
        while (this.connectionStatus != ConnectionStatus.ONLINE) {
            try {
                this.server = connectToHost();
            } catch (IOException e) {
                this.connectionStatus = ConnectionStatus.OFFLINE;
                //TODO: Log connection fail
            }
            try {
                Thread.sleep(receiverConfig.getTimeTillReconnect());
            } catch (InterruptedException e) {
                //Todo: Is handling the exception necessary?
            }
        }
        try {
            handleConnection();
        } catch (IOException e) {
            //Todo: handle error
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

    private void handleConnection() throws IOException {
        InputStream in = server.getInputStream();
        while (getConnectionStatus() == ConnectionStatus.ONLINE) {
            Future<byte[]> currentTelegram = parseConnection(in);
            while (!currentTelegram.isDone()) {
                //Parse received telegrams while waiting for next
                if (!telegramQueue.isEmpty()) {
                    parseTelegram(telegramQueue.get(0));
                    telegramQueue.remove(0);
                }
                else
                        Thread.yield();
            }
            try {
                telegramQueue.add(currentTelegram.get());
            } catch (InterruptedException e) {
                //Todo: handle
                e.printStackTrace();
            } catch (ExecutionException e) {
                //Todo: handle
                e.printStackTrace();
            }
        }
    }

    @Async
    private Future<byte[]> parseConnection(InputStream in) throws IOException {
        int readPos = 0, maxResponseLength = 255;
        byte[] response = new byte[maxResponseLength];
        while (readPos < maxResponseLength) {
            int responseLength = in.read(response, readPos, maxResponseLength - readPos);
            readPos += responseLength;
        }
        //packet read to response
        parseTelegram(response);
        return new AsyncResult<>(response);
    }

    private void parseTelegram(byte[] response) {
        //Todo: add real parser logic
        //Todo: response is 0000000... if connection ended
        for (int i = 0; i < 3; ++i) {
            if (response[i] != (byte) 0xFF) {
                throw (new RuntimeException("Byte " + i + " hat falsches Format: " + response[i]));
            }
        }
        int messageLength = response[3];
        for (int i = 4; i < 3 + messageLength; ++i) {
            if (i == 4) {
                //Typangabe
            }
            System.out.println("Byte " + i + ": " + response[5]);
        }
    }

    public Socket connectToHost() throws IOException {
        connectionStatus = ConnectionStatus.CONNECTING;
        SocketAddress hostAddress = new InetSocketAddress(receiverConfig.getHostname(), receiverConfig.getPort());
        Socket socket = new Socket();
        socket.connect(hostAddress, receiverConfig.getTimeout());
        this.connectionStatus = ConnectionStatus.ONLINE;
        return socket;
    }

    /** returns the ConnectionStatus of the parser
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
        this.connectionStatus = connectionStatus;
    }
}
