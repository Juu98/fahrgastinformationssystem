package fis.telegramReceiver;

import fis.telegrams.ByteConversions;
import fis.telegrams.SendableTelegram;
import fis.telegrams.TelegramPart;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Future;

/**
 * Verantwortlich für Verarbeitung der Input- und Outputstreams zum/vom Telegrammserver
 * Senden und Empfangen der Telegrmmrohdaten
 */
@Component
public class TelegramReceiver {
	private final TelegramReceiverConfig receiverConfig;

	/**
	 * Konstruktor, benötigt eine receiverConfig
	 *
	 * @param config TelegramreceiverController mit Konfigurationsdaten
	 */
	@Autowired
	public TelegramReceiver(TelegramReceiverConfig config) {
		this.receiverConfig = config;
	}

	/**
	 * Asnchroner Empfang von Telegrammrohdaten aus dem InputStream in
	 *
	 * @param in (Inputstream)
	 * @return response
	 * in ein AsyncResult<byte[]> verpackte Telegrammrohdaten
	 * @throws IOException
	 */
	@Async
	Future<byte[]> parseConnection(InputStream in) throws IOException {
		byte[] response = new byte[TelegramPart.RAW_DATA.maxLength()];
		int pos = 0;
		while (pos < 3) {
			//read one byte and look whether it is 0xFF, which marks beginning of a new Telegram
			in.read(response, pos, 1);
			//throw away invalid data
			if (response[pos] != ByteConversions.toUByte(0xFF)) {
				//reset telegram, start again
				if (pos > 0) {
					pos = 0;
				}
				continue;
			}
			pos++;
		}
		//read length byte
		in.read(response, pos, 1);
		int length = ByteConversions.toUInt(response[pos]);
		//read telegram data
		in.read(response, ++pos, length);
		return new AsyncResult<>(response);
	}

	/**
	 * verschickt ein SendableTelegram in Rohdatenform an den Telegrammserver
	 *
	 * @param out OutputStream, auf den das Telegramm geschrieben wird
	 * @param telegram ein SendableTelegramm
	 * @throws IOException
	 */
	public void sendTelegram(OutputStream out, SendableTelegram telegram) throws IOException {
		out.write(telegram.getRawTelegram());
	}

}
