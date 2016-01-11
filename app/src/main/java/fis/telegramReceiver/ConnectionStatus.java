package fis.telegramReceiver;

/**
 * Gibt den Verbindungsstatus zum Telegrammserver an
 */
public enum ConnectionStatus {
    CONNECTING,
    OFFLINE,
    ONLINE,
    INIT    //Verbindung besteht, Telegramme werden empfangen, aber noch kein Trainroute End telegram
}
