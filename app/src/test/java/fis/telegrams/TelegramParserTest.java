package fis.telegrams;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by spiollinux on 06.12.15.
 */
public class TelegramParserTest {
	byte[] validRawTelegram;
	TelegramParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new TelegramParser();
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
					invalidRawTelegram[i] = (byte) (( (int) Math.ceil(Math.random()*255) & 0xFF));
				} while( ((byte) 255) == invalidRawTelegram[i]);
			}
			else
				invalidRawTelegram[i] = (byte) ( (int) Math.ceil(Math.random()*255));
		}

		try {
			parser.parse(invalidRawTelegram);
			fail("First 3 telegram bytes are invalid and should throw an exception");
		} catch (TelegramParseException e) {
			//test pass
		}
	}

	@Test
	public void testLabTimeTelegramParse() {
		//create test rawTelegram
		validRawTelegram[4] = (byte) (241 & 0xFF);  //& 0xFF to emulate unsigned bytes with java's signed bytes
		//Todo: add valid data here
		validRawTelegram[5] = (byte) (1 & 0xFF);
		validRawTelegram[6] = (byte) (59 & 0xFF);
		validRawTelegram[7] = (byte) (59 & 0xFF);
		//Todo: set length
		validRawTelegram[3] = (byte) (6 & 0xFF);

		LabTimeTelegram parsedTelegram = null;
		try {
			parsedTelegram = (LabTimeTelegram) parser.parse(validRawTelegram);
		} catch (TelegramParseException e) {
			fail("ParseException: " + e.getMessage());
		}
		LabTimeTelegram referenceTelegram = new LabTimeTelegram(LocalTime.of(1,59,59));
		assertTrue("parsed LabTimeTelegram doesn't match the expected one",referenceTelegram.equals(parsedTelegram));
	}
}