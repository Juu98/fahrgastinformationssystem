package fis.telegrams;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by spiollinux on 08.11.15.
 */
@Component
@ConfigurationProperties(prefix = "telegramserver")
public class TelegramReceiverConfig {
    private String hostname;
    private int port;
    private byte clientID;
    private int timeout = 1000;
    private int timeTillReconnect = 5000;

    public int getTimeTillReconnect() {
        return timeTillReconnect;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public byte getClientID() {
        return clientID;
    }

    public void setClientID(byte clientID) {
        this.clientID = clientID;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
