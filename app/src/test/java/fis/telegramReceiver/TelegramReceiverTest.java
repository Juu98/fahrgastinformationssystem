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
import fis.telegrams.RegistrationTelegram;
import fis.telegrams.SendableTelegram;
import org.hibernate.engine.transaction.synchronization.internal.RegisteredSynchronization;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

/**
 * Testet die Funtionalität der Klasse TelegramReceiver
 * @author spiollinux
 */
public class TelegramReceiverTest {
	TelegramReceiver receiver;
	InputStream in;

	@Before
	public void setUp() {
		receiver = new TelegramReceiver(new TelegramReceiverConfig());
	}

	/**
	 * Prüft, ob aus Bytestream Rohtelegrammdaten isoliert werden können
	 * @throws Exception
	 */
	@Test
	public void testParseConnection() throws Exception {
		byte[] rawData = new byte[200];
		//bogus bytes
		rawData[1] = ByteConversions.toUByte(0xFF);
		rawData[1] = ByteConversions.toUByte(12);
		rawData[1] = ByteConversions.toUByte(0xFF);
		//start bytes
		rawData[20] = ByteConversions.toUByte(0xFF);
		rawData[21] = ByteConversions.toUByte(0xFF);
		rawData[22] = ByteConversions.toUByte(0xFF);
		//length byte
		rawData[23] = ByteConversions.toUByte(10);
		//irrelevant byte
		rawData[145] = 23;
		byte[] referenceData = new byte[255];
		referenceData[0] = ByteConversions.toUByte(0xFF);
		referenceData[1] = ByteConversions.toUByte(0xFF);
		referenceData[2] = ByteConversions.toUByte(0xFF);
		referenceData[3] = ByteConversions.toUByte(10);
		in = new ByteArrayInputStream(rawData);
		Future<byte[]> parsedData = receiver.parseConnection(in);
		byte[] parsedRawData = parsedData.get();
		assertArrayEquals("received raw telegram doesn't match expected one",referenceData,parsedRawData);
	}

	@Test
	public void testSendTelegram() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(255);
		SendableTelegram tele = new RegistrationTelegram((byte) 42);
		receiver.sendTelegram(out, tele);
		out.flush();
		assertArrayEquals(tele.getRawTelegram(), out.toByteArray());
	}
}
