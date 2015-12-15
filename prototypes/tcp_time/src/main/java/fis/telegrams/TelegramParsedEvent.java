package fis.telegrams;

import org.springframework.context.ApplicationEvent;

/**
 * Created by spiollinux on 05.12.15.
 */
public class TelegramParsedEvent extends ApplicationEvent{

	private Telegram source;
	/**
	 * Create a new ApplicationEvent.
	 *
	 * @param source the object on which the event initially occurred (never {@code null})
	 */
	public TelegramParsedEvent(Telegram source) {
		super(source);
	}

	public Telegram getSource() {
		return this.source;
	}
}
