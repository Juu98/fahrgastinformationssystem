package fis.telegrams;

import java.io.UnsupportedEncodingException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
	
	public static byte[] fromIntArr(int[] arr){
		byte[] ret = new byte[arr.length];
		for (int i = 0; i<arr.length; i++){
			ret[i] = (byte) (arr[i] & 0xFF);
		}
		return ret;
	}
	
	public static byte[] fromString(String s){
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
	public static byte[] fromString(String... arr){
		return fromString(String.join("", arr));
	}	
	
	public static String toByteString(byte b){
		return String.format("%02X", b);
	}
	public static String toByteString(byte[] arr){
		return toByteString(arr, 0);
	}
	public static String toByteString(byte[] arr, int len){
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
		validRawTelegram[3] = (byte) (4 & 0xFF);

		LabTimeTelegram parsedTelegram = (LabTimeTelegram) parser.parse(validRawTelegram);

		LabTimeTelegram referenceTelegram = new LabTimeTelegram(LocalTime.of(1,59,59));
		Assert.assertEquals("Laborzeittelegramme stimmen nicht überein.", referenceTelegram, parsedTelegram);
	}
	
	@Test(expected = TelegramParseException.class)
	public void testLabTimeTelegramValid() throws TelegramParseException {
		byte[] invalidRawTelegram = fromString("FF FF FF  04  F1  19 FF FE");
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
				toByteString((byte) ((name.length + code.length + 3) & 0xFF)),
				"EE",
				toByteString(id),
				toByteString((byte) (code.length & 0xFF)),
				toByteString(code),
				toByteString(name)
		);
		StationNameTelegram telegram = (StationNameTelegram) parser.parse(validRawTelegram);
		StationNameTelegram referenceTelegram = new StationNameTelegram(id, c, n);
		
		Assert.assertEquals("BS-Bezeichnungstelegramme stimmen nicht überein", referenceTelegram, telegram);
	}
	
	// TODO validation tests
	
	/* ZEIT-KONVERTIERUNG */
	
	@Test
	public void testFromTenthOfMinute() throws TelegramParseException{
		LocalTime ref = LocalTime.of(13,37,42);
		int secs = (13*60 + 37)*60 + 42;
		// zum Glück ist 42 durch 6 teilbar...
		int tenth = secs / 6;
		Assert.assertEquals("Zeiten stimmen nicht überein.", ref, TelegramParser.fromTenthOfMinute(tenth));
	}
	
	@Test
	public void testTimeDifferencePositive() throws TelegramParseException{
		LocalTime ref = LocalTime.of(13,37,42);
		int secs = 120*60;
		int tenth = secs / 6;
		Assert.assertEquals("Zeiten stimmen nicht überein.", ref.plusSeconds(secs), TelegramParser.fromTenthOfMinute(tenth, ref));
	}
	
	@Test
	public void testTimeDifferenceNegative() throws TelegramParseException{
		LocalTime ref = LocalTime.of(13,37,42);
		int secs = 120*60;
		int tenth = -secs / 6;
		Assert.assertEquals("Zeiten stimmen nicht überein.", ref.minusSeconds(secs), TelegramParser.fromTenthOfMinute(tenth, ref));
	}
	
	/* ZUGLAUFTELEGRAMM */
	
	@Test
	public void testTrainRouteTelegram() throws TelegramParseException{
		List<TrainRouteTelegram.StopData> stops = new ArrayList<>(3);
		// #01 12:00 12:08 +0min +0min 7 - - -
		// 12*60*10 = 7200 = 0x1C20 Big Endian
		String stop1 = "01 1C 20 1C 70 00 00 00 00 07 00 00 00";
		stops.add(new TrainRouteTelegram.StopData(
				1, LocalTime.of(12, 0), LocalTime.of(12, 8), LocalTime.of(12, 0), LocalTime.of(12, 8),
				7, 0, 0, 0)
		);
		// #13 12:34 12:40 +1min -3min 1 3 1 1
		// (12*60+34)*10 = 7540 = 0x1D74
		String stop2 = "0D 1D 74 1D B0 00 0A FF E2 01 03 01 01";
		stops.add(new TrainRouteTelegram.StopData(
				13, LocalTime.of(12, 34), LocalTime.of(12, 40), LocalTime.of(12, 35), LocalTime.of(12, 37),
				1, 3, 1, 1)
		);
		/*// #42 13:37 --   +10min --   10 - - -
		// (13*60+37)*10 = 8170 = 0x1FEA
		String stop3 = "01 1F EA 00 00 64 00 0A 00 00 00";
		stops.add(new TrainRouteTelegram.StopData(
				42, LocalTime.of(13, 37), null, LocalTime.of(13, 47), null,
				10, 0, 0, 0)
		);*/
		
		byte[] testData = fromString(
			"00 04",
			toByteString("1234".getBytes(TelegramParser.CHARSET), 6),
			"00 DE AD BE EF",
			"03",
			toByteString("ICE".getBytes(TelegramParser.CHARSET), 6),
			"2A 00 C0 FF EE",
			"00 02",
			stop1, stop2//, stop3
		);
		validRawTelegram = fromString(
			"FF FF FF",
			toByteString((byte) (testData.length + 1)),
			"EC",
			toByteString(testData)
		);
		
		TrainRouteTelegram expected = new TrainRouteTelegram(
			new TrainRouteTelegram.TrainRouteData("1234", "ICE", 0,	stops)
		);
		
		Assert.assertEquals("Telegramme stimmen nicht überein.", expected, parser.parse(validRawTelegram));
	}		
}