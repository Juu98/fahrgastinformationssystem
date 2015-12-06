package fis.telegrams;

import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

/**
 * Created by spiollinux on 06.12.15.
 */
public class TelegramTest {

	byte[] validRawTelegram;
	@Before
	public void setUp() throws Exception {
		validRawTelegram = new byte[Telegram.rawTelegramLength];
		for(int i = 0; i < 3; ++i) {
			validRawTelegram[i] = (byte) 255;
		}
	}

	@Test
	public void testFirstBytesValid() {
		int r = (int) Math.floor(Math.random()*3);
		//initialize invalid byte data
		byte[] invalidRawTelegram = new byte[Telegram.rawTelegramLength];
		for(int i = 0; i < 3; i++) {
			//force at least one bit to be != 255, the others are set randomly
			if(i == r) {
				do {
					invalidRawTelegram[i] = (byte) ( (int) Math.ceil(Math.random()*255));
				} while( ((byte) 255) == invalidRawTelegram[i]);
			}
			else
				invalidRawTelegram[i] = (byte) ( (int) Math.ceil(Math.random()*255));
		}

		try {
			Telegram.parse(invalidRawTelegram);
			fail("First 3 telegram bytes are invalid and should throw an exception");
		} catch (TelegramParseException e) {
			//test pass
		}
	}

	@Test
	public void testLabTimeTelegramParse() {
		//create test rawTelegram
		validRawTelegram[4] = (byte) 241;
		//Todo: add valid data here
		//Todo: set length
		fail("test not fully implemented");
	}
}