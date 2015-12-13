package fis.telegrams;

import java.nio.charset.Charset;
import java.time.DateTimeException;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Arrays;

/**
 * Verarbeitet die rohen Telegrammdaten.
 * 
 * <p> Telegramme sind nach folgender Struktur aufgebaut:
 *	 0 - 2: {@literal FF FF FF} Startkennung,
 *		 3: {@literal XX} Länge der Nutzdaten
 *		 4: {@literal TT} Kennung des Telegrammtyps
 * 5- XX+5: Nutzdaten (immer 251 B lang, vermutlihc mit {@literal 00} aufgefüllt.
 * 
 * Created by spiollinux on 11.12.15.
 */
@Component
public class TelegramParser {
	/** Konstanten der Telegrammspezifikation.
	 * T ... Telegramm
	 * B ... Byte
	 * TC ... Telegrammtyp
	 */
	// Telegrammspezifikation
	private static final int T_MAXLENGTH   = Telegram.rawTelegramLength;
	private static final int T_STARTBCOUNT = 3;
	private static final int T_DATALENPOS  = 3;
	private static final int T_CATPOS	   = 4;
	private static final int T_DATAPOS	   = 2+T_STARTBCOUNT;

	// Startkennung (Bytes 0-T_STARTBCOUNT im Telegramm)
	private static final byte B_START	   = (byte) (255 & 0xFF);	// 0xFF

	// Telegrammkategorie-Konstanten (Byte 1 im Telegramm)
	private static final byte B_TC_LABTIME = (byte) (241 & 0xFF);	// 0xF1
	private static final byte B_TC_TRAINRT = (byte) (236 & 0xFF);	// 0xEC
	private static final byte B_TC_TRNEND  = (byte) (236 & 0xFF);	// TODO duplicate category 0xEC
	private static final byte B_TC_STATION = (byte) (238 & 0xFF);	// 0xEE
	
	// Codepage für String-Konvertierung
	private static final Charset CHARSET = Charset.forName("ISO-8859-1");
	
	/**
	 * Konvertiert eine Ganzzahl in ein vorzeichenloses Byte
	 * @param i	die umzuwandelnde Ganzzahl
	 * @return das letzte Byte (LSB) dieser Zahl
	 */
	protected static byte toUByte(int i){
		return (byte) (i & 0xFF);
	}
	
	/**
	 * Konvertiert ein Byte in eine positive ganze Zahl
	 * @param b das umzuwandelnde Byte
	 * @return die ganze Zahl (0 &lt;= x &lt;= 255) mit dem übergeben als letztem Byte
	 */
	protected static int toInt(byte b){
		return (int) b;
	}
	
	/**
	 * Übersetzt ein empfangenes Bytearray in ein Telegramm.
	 * @param rawResponse empfangene Bytefolge mit maximaler Länge 255
	 * @return spezifische Instanz eines {@link Telegram}
	 * @throws TelegramParseException wenn bei der Verarbeitung ein Fehler auftritt.
	 */
	public <T extends Telegram> T parse(byte[] rawResponse) throws TelegramParseException {
		// Telegrammlänge prüfen
		if (rawResponse == null){
			throw new TelegramParseException("Versuch 'null' zu parsen.");
		} else if (rawResponse.length == 0){
			throw new TelegramParseException("Versuch ein leeres Bytearray zu parsen.");
		} else if (rawResponse.length < T_DATAPOS){
			throw new TelegramParseException(String.format("Bytearray ist kürzer (%d B) als erwartet (%d B).", rawResponse.length, T_DATAPOS));
		} else if (rawResponse.length > T_MAXLENGTH){
			throw new TelegramParseException(String.format("Bytearray ist länger (%d B) als erwartet (%d B).", rawResponse.length, T_MAXLENGTH));
		}
		
		// Startkennung
        for (int i = 0; i < T_STARTBCOUNT; i++) {
            if (rawResponse[i] != B_START) {
                throw new TelegramParseException(String.format("Byte %d (%0#4x) hat falsches Format, %0#4x erwartet.", i, rawResponse[i], B_START));
            }
        }
		
		// Nutzdaten extrahieren
        int dataLength = (rawResponse[T_DATALENPOS] & 0xFF);
		if (rawResponse.length < T_DATAPOS+dataLength){
			throw new TelegramParseException(String.format("Bytearray ist kürzer (%d) als angegeben (%d).", rawResponse.length, T_DATAPOS+dataLength));
		}
		byte[] data = Arrays.copyOfRange(rawResponse, T_DATAPOS, T_DATAPOS+dataLength);
		
		//Todo: add real parser logic
        //Todo: response is 0000000... if connection ended

		// Telegramm-Kategorien
		switch (rawResponse[T_CATPOS]) {
			case B_TC_LABTIME: return (T) parseLabTimeData(data);
			case B_TC_TRAINRT: return (T) parseTrainRouteData(data);
			// TODO duplicate category!! case B_TC_TRNEND : return parseTrainRouteEndData(data);
			case B_TC_STATION: return (T) parseStationNameData(data);
			
			default:
				throw new TelegramParseException(String.format("Unbekannte Kategorie: %0#4x", rawResponse[T_CATPOS]));
		}
	}
	
	/**
	 * Verarbeitet ein Laborzeittelegramm
	 * @param rawData die empfangene Rohdaten, mindestens 3 bytes
	 * @return ein fertiges Laborzeittelegramm
	 * @throws TelegramParseException 
	 */
	private LabTimeTelegram parseLabTimeData(byte[] rawData) throws TelegramParseException {
		// mindestens drei Byte (h, min, sec)
		final int MIN_LENGTH = 3;
		if(rawData.length < MIN_LENGTH) {
			throw new TelegramParseException(String.format("Bytearray für Laborzeittelegramm ist kürzer (%d) als erwartet (%d)", rawData.length, MIN_LENGTH));
		}
		
		// Werte auslesen
		LocalTime t;
		try {
			t = LocalTime.of(toInt(rawData[0]), toInt(rawData[1]), toInt(rawData[2]));
		} catch (DateTimeException e){
			throw new TelegramParseException("Fehler beim Verarbeiten der Zeitangaben: "+e);
		}
		
		return new LabTimeTelegram(t);
	}
	
	private TrainRouteTelegram parseTrainRouteData(byte[] data){
		throw new UnsupportedOperationException();
	}
	private TrainRouteEndTelegram parseTrainRouteEndData(byte[] data){
		throw new UnsupportedOperationException();
	}
	
	private StationNameTelegram parseStationNameData(byte[] rawData) throws TelegramParseException{
		final int MAX_CODE_LENGTH = 5;
		final int MIN_LENGTH = MAX_CODE_LENGTH + 2;
		
		if(rawData.length < MIN_LENGTH) {
			throw new TelegramParseException(String.format("Bytearray für BS-Bezeichnungstelegramm ist kürzer (%d) als erwartet (%d)", rawData.length, MIN_LENGTH));
		}
		
		final int codeLength = toInt(rawData[1]);
		if (codeLength > MAX_CODE_LENGTH){
			throw new TelegramParseException(String.format("Abkürzung länger (%d) als erlaubt (%d).", codeLength, MAX_CODE_LENGTH));
		}
		final String code = new String(Arrays.copyOfRange(rawData, 2, codeLength+2), CHARSET);
		final String name = new String(Arrays.copyOfRange(rawData, MIN_LENGTH, rawData.length), CHARSET);
		
		return new StationNameTelegram(rawData[0], code, name);
	}
}
