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
 * @author schmittlauch
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