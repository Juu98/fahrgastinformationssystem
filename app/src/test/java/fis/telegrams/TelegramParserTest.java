package fis.telegrams;

import java.io.UnsupportedEncodingException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import org.junit.Assert;


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
	
	private byte[] fromIntArr(int[] arr){
		byte[] ret = new byte[arr.length];
		for (int i = 0; i<arr.length; i++){
			ret[i] = (byte) (arr[i] & 0xFF);
		}
		return ret;
	}
	
	private byte[] fromString(String s){
		if (s.startsWith("0x")){
			s = s.substring(2);
		}
		s = s.replaceAll("\\s", "");
		if (s.length() % 2 != 0){
			throw new IllegalArgumentException("Not a valid Byte-String (uneven number of nibbles!");
		}
		
		byte[] ret = new byte[s.length() / 2];
		for (int i=0; i < ret.length; i++){
			ret[i] = (byte) (Integer.parseInt(s.substring(2*i, 2*i+2), 16) & 0xFF);
		}
		
		return ret;
	}
	private byte[] fromString(String... arr){
		return fromString(String.join("", arr));
	}
	
	private String toByteString(byte b){
		return String.format("%02X", b);
	}
	private String toByteString(byte[] arr){
		return toByteString(arr, 0);
	}
	private String toByteString(byte[] arr, int len){
		String s = "";
		for (byte b : arr){
			s += toByteString(b) + " ";
		}
		
		while (arr.length < len){
			s += "00 ";
			len--;
		}
		
		return s.trim();
	}
	
	
	/* GENERELLE STRUKTUR */

	@Test(expected = TelegramParseException.class)
	public void testStartBytesValid() throws TelegramParseException {
		int r = (int) Math.floor(Math.random()*3);
		//initialize invalid byte data
		byte[] invalidRawTelegram = new byte[Telegram.rawTelegramLength];
		for(int i = 0; i < 3; i++) {
			//force at least one byte to be != 255, the others are set randomly
			if(i == r) {
				do {
					invalidRawTelegram[i] = (byte) (( (int) Math.ceil(Math.random()*255) & 0xFF));
				} while( ((byte) 255) == invalidRawTelegram[i]);
			}
			else
				invalidRawTelegram[i] = (byte) ( (int) Math.ceil(Math.random()*255));
		}

		parser.parse(invalidRawTelegram);
	}

	@Test(expected = TelegramParseException.class)
	public void testNullTelegram() throws TelegramParseException {
		parser.parse(null);
	}
	
	@Test(expected = TelegramParseException.class)
	public void testEmptyTelegram() throws TelegramParseException {
		parser.parse(new byte[0]);
	}
	
	@Test(expected = TelegramParseException.class)
	public void testShortTelegram() throws TelegramParseException {
		parser.parse(fromIntArr(new int[] {0xFF, 0xFF, 0xFF}));
	}
	
	@Test(expected = TelegramParseException.class)
	public void testLongTelegram() throws TelegramParseException {
		parser.parse(new byte[1337]);
	}
	
	/* LABORZEITTELEGRAMM */
	
	@Test
	public void testLabTimeTelegramParse() throws TelegramParseException {
		// create test rawTelegram
		validRawTelegram[4] = (byte) (241 & 0xFF); 
		// add valid data here
		validRawTelegram[5] = (byte) (1 & 0xFF);
		validRawTelegram[6] = (byte) (59 & 0xFF);
		validRawTelegram[7] = (byte) (59 & 0xFF);
		// set length
		validRawTelegram[3] = (byte) (6 & 0xFF);

		LabTimeTelegram parsedTelegram = (LabTimeTelegram) parser.parse(validRawTelegram);

		LabTimeTelegram referenceTelegram = new LabTimeTelegram(LocalTime.of(1,59,59));
		Assert.assertEquals("Laborzeittelegramme stimmen nicht überein.", referenceTelegram, parsedTelegram);
	}
	
	@Test(expected = TelegramParseException.class)
	public void testLabTimeTelegramValid() throws TelegramParseException {
		byte[] invalidRawTelegram = fromString("FF FF FF  03  F1  19 FF FE");
		parser.parse(invalidRawTelegram);
	}
	
	/* BS-BEZEICHNUNGSTELEGRAMM */
	@Test
	public void testStationNameTelegram() throws UnsupportedEncodingException, TelegramParseException{
		String c = "ENT";
		String n = "Entenhausen";
		
		byte[] name = n.getBytes("ISO-8859-1");
		byte[] code = c.getBytes("ISO-8859-1");
		byte id = (byte) 0x2A;
		
		validRawTelegram = fromString(
				"FF FF FF",
				toByteString((byte) ((name.length + 7) & 0xFF)),
				"EE",
				toByteString(id),
				toByteString((byte) (code.length & 0xFF)),
				toByteString(code, 5),
				toByteString(name)
		);
		StationNameTelegram telegram = (StationNameTelegram) parser.parse(validRawTelegram);
		StationNameTelegram referenceTelegram = new StationNameTelegram(id, c, n);
		
		// assertEquals fails for reasons
		Assert.assertEquals("BS-Bezeichnungstelegramme stimmen nicht überein", referenceTelegram, telegram);
	}
}