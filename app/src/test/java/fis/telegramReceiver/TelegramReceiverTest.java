package fis.telegramReceiver;

import fis.telegrams.TelegramParser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

/**
 * Created by spiollinux on 20.12.15.
 */
public class TelegramReceiverTest {
	TelegramReceiver receiver;
	InputStream in;

	@Before
	public void setUp() {
		receiver = new TelegramReceiver(new TelegramReceiverConfig());
	}

	@Test
	public void testParseConnection() throws Exception {
		byte[] rawData = new byte[200];
		rawData[1] = TelegramParser.toUByte(0xFF);
		rawData[1] = TelegramParser.toUByte(12);
		rawData[1] = TelegramParser.toUByte(0xFF);
		//start bytes
		rawData[20] = TelegramParser.toUByte(0xFF);
		rawData[21] = TelegramParser.toUByte(0xFF);
		rawData[22] = TelegramParser.toUByte(0xFF);
		//length byte
		rawData[23] = TelegramParser.toUByte(10);
		//irrelevant byte
		rawData[145] = 23;
		byte[] referenceData = new byte[255];
		referenceData[0] = TelegramParser.toUByte(0xFF);
		referenceData[1] = TelegramParser.toUByte(0xFF);
		referenceData[2] = TelegramParser.toUByte(0xFF);
		referenceData[3] = TelegramParser.toUByte(10);
		in = new ByteArrayInputStream(rawData);
		Future<byte[]> parsedData = receiver.parseConnection(in);
		byte[] parsedRawData = parsedData.get();
		assertArrayEquals("received raw telegram doesn't match expected one",referenceData,parsedRawData);
	}
}