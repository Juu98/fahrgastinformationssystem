package fis.telegrams;

import fis.telegramReceiver.ConnectionStatus;
import org.springframework.context.ApplicationEvent;

/**
 * Created by spiollinux on 11.12.15.
 */
public class ConnectionStatusEvent extends ApplicationEvent {
	private ConnectionStatus source;
	/**
	 * Create a new ApplicationEvent.
	 *
	 * @param source the object on which the event initially occurred (never {@code null})
	 */
	public ConnectionStatusEvent(ConnectionStatus source) {
		super(source);
	}

	@Override
	public ConnectionStatus getSource() {
		return this.source;
	}
}
