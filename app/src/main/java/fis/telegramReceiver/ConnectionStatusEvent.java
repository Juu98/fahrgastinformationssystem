package fis.telegramReceiver;

import org.springframework.context.ApplicationEvent;

/**
 * Event, das auf eine Änderung des Verbindungsstatus des TelegramReceiverControllers zum Server hinweist
 */
public class ConnectionStatusEvent extends ApplicationEvent {
	private ConnectionStatus status;

	/**
	 * Erstellt ein neues ConnectionStatusEvent als Kapselung für einen {@link ConnectionStatus}
	 *
	 * @param status
	 */
	public ConnectionStatusEvent(ConnectionStatus status) {
		super(status);
		this.status = status;
	}

	/**
	 * gibt den Status in das Event gekapselten ConnectionStatus zurück
	 *
	 * @return ConnectionStatus
	 */
	public ConnectionStatus getStatus() {
		return status;
	}
}
