/* Copyright 2016 Eric Schölzel, Robert Mörseburg, Zdravko Yanakiev, Jonas Schenke, Oliver Schmidt
 *
 * This file is part of FIS.
 *
 * FIS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FIS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FIS.  If not, see <http://www.gnu.org/licenses/>.
 */
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
	 * Asynchroner Empfang von Telegrammrohdaten aus dem InputStream in
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
