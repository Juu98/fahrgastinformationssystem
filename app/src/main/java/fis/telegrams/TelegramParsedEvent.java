package fis.telegrams;

import org.springframework.context.ApplicationEvent;

/**
 * Event, das nach dem erfolgreichen Parsen eines Telegramms ausgelöst wird. Kapselt ein {@see Telegram} Objekt
 */
public class TelegramParsedEvent extends ApplicationEvent {

	/**
	 * Erstellt ein neues TelegramParsedEvent
	 *
	 * @param source ein Telegram Objekt, das mit diesem Event übermittelt werden soll
	 */
	public TelegramParsedEvent(Telegram source) {
		super(source);
		this.source = source;
	}

	public Telegram getSource() {
		return (Telegram) this.source;
	}
}
