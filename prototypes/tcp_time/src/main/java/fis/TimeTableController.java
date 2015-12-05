package fis;

import fis.telegrams.Telegram;
import fis.telegrams.TelegramParsedEvent;
import org.springframework.context.event.EventListener;

/**
 * just a mock class
 */
public class TimeTableController {

	/**
	 * fügt Telegrammdaten in unsere Fahrplandatenstruktur ein
	 * wird automatisch aufgerufen, wenn ein TelegramParsedEvent gepublished wird.
	 * synchroner Aufruf, läuft daher im TelegramReceiver Thread => Locking notwendig
	 * @param event
	 */
	@EventListener
	public void forwardTelegram(TelegramParsedEvent event) {
		Telegram receivedTelegram = event.getSource();
		//do things with the telegram
	}
}
