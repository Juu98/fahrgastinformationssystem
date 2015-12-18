package fis.telegrams;

import java.nio.charset.Charset;
import java.time.DateTimeException;
import java.time.Duration;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Verarbeitet die rohen Telegrammdaten.
 * 
 * <p> Telegramme sind nach folgender Struktur aufgebaut:
 *	 0 - 2: {@literal FF FF FF} Startkennung,
 *		 3: {@literal XX} Länge der Nutzdaten inklusive Kennung
 *		 4: {@literal TT} Kennung des Telegrammtyps
 * 5- XX+5: Nutzdaten (immer 251 B lang, vermutlihc mit {@literal 00} aufgefüllt.
 * 
 * Created by spiollinux on 11.12.15.
 */
@Component
public class TelegramParser {
	private static final Logger LOGGER = Logger.getLogger(TelegramParser.class);
	/** Konstanten der Telegrammspezifikation.
	 * T ... Telegramm
	 * B ... Byte
	 * TC ... Telegrammtyp
	 */
	// Telegrammspezifikation
	private static final int T_MAXLENGTH   = Telegram.rawTelegramLength;
	private static final int T_STARTBCOUNT = 3;
	private static final int T_DATALENPOS  = T_STARTBCOUNT;
	private static final int T_CATPOS	   = T_STARTBCOUNT+1;
	private static final int T_DATAPOS	   = T_STARTBCOUNT+2;

	// Startkennung (Bytes 0-T_STARTBCOUNT im Telegramm)
	private static final byte B_START	   = (byte) (255 & 0xFF);	// 0xFF

	// Telegrammkategorie-Konstanten (Byte 1 im Telegramm)
	private static final byte B_TC_LABTIME = (byte) (241 & 0xFF);	// 0xF1
	private static final byte B_TC_TRAINRT = (byte) (236 & 0xFF);	// 0xEC
	private static final byte B_TC_TRENDMK = (byte) (255 & 0xFF);	// 0xFF
	private static final byte B_TC_STATION = (byte) (238 & 0xFF);	// 0xEE
	
	// Codepage für String-Konvertierung
	public static final Charset CHARSET = Charset.forName("ISO-8859-1");
	// TODO Endianness für die Wort-Konvertierung bei Zeiten
	public static final boolean LITTLE_ENDIAN = false;
	// TODO Behandlung von null-Werten
	public static final boolean PRESERVE_NULLS = false;
	
	/**
	 * Konvertiert eine Ganzzahl in ein vorzeichenloses Byte.
	 * @param i	die umzuwandelnde Ganzzahl
	 * @return das letzte Byte (LSB) dieser Zahl
	 */
	public static byte toUByte(int i){
		return (byte) (i & 0xFF);
	}
	
	/**
	 * Konvertiert ein Byte in eine vorzeichenbehaftete ganze Zahl.
	 * @param b das Byte
	 * @return die ganze Zahl (-128 &lt; x &lt; 127)
	 */
	public static int toInt(byte b){
		return (int) b;
	}
	
	/**
	 * Konvertiert ein Word in eine vorzeichenbehaftete Ganzzahl.
	 * @param b0	erstes Byte
	 * @param b1	zweites Byte
	 * @param littleEndian	Bytereihenfolge
	 * @return die ganze Zahl
	 */
	public static int toInt(byte b0, byte b1, boolean littleEndian){
		if (littleEndian) return (toInt(b1) << 8) | toInt(b0);
		return (toInt(b0) << 8) | b1;
	}
	
	/**
	 * Konvertiert ein Byte in eine positive ganze Zahl.
	 * @param b das umzuwandelnde Byte
	 * @return die ganze Zahl (0 &lt;= x &lt;= 255) mit dem übergeben als letztem Byte
	 */
	public static int toUInt(byte b){
		return ((int) b) & 0xFF;
	}
	
	/**
	 * Konvertiert ein Wort in eine positive ganze Zahl.
	 * @param b0 erstes Byte
	 * @param b1 zweites Byte
	 * @param littleEndian Bytereihenfolge
	 * @return die ganze Zahl (0 &lt; x &lt; 512) mit den übergebenen Bytes als LSBs
	 */
	public static int toUInt(byte b0, byte b1, boolean littleEndian){
		if (littleEndian) return (toUInt(b1) << 8) | toUInt(b0);
		return (toUInt(b0) << 8) | toUInt(b1);
	}
	
	/**
	 * Berechnet einen Zeitpunkt aus Zehnetlminutenangaben.
	 * @param tenth	Die Zehntelminuten
	 * @return der Zeitpunkt
	 * @throws TelegramParseException wenn negative Zehntelminuten übergeben werden.
	 */
	public static LocalTime fromTenthOfMinute(int tenth) throws TelegramParseException{
		return fromTenthOfMinute(tenth, null);
	}
	
	/**
	 * Berechnet einen zeitpunkt aus dem Unterschied zu einem Referenzzeitpunkt.
	 * @param tenth	der Zeitunterschied in Zehntelminuten (kann auch negativ sein)
	 * @param base der Referenzzeitpunkt oder {@literal null}, wenn nur ein Zeitpunkt bestimmt werden soll.
	 * @return berechneter Zeitpunkt
	 * @throws TelegramParseException wenn ein negativer Zeitunterschied ohne Referenzzeitpunkt angegeben wurde.
	 */
	public static LocalTime fromTenthOfMinute(int tenth, LocalTime base) throws TelegramParseException{
		boolean isNegative = (tenth < 0);
		tenth = (isNegative) ? -tenth : tenth;
		
		if (base == null){
			if (isNegative){
				throw new TelegramParseException("Zeitpunkt kann nicht negativ sein.");
			}
			return LocalTime.ofSecondOfDay(tenth*6);
		}
		
		Duration delta = Duration.ofSeconds(tenth*6);
		return (isNegative) ? base.minus(delta) : base.plus(delta);
	}
	
	/**
	 * Übersetzt ein empfangenes Bytearray in ein Telegramm.
	 * @param rawResponse empfangene Bytefolge mit maximaler Länge 255
	 * @return spezifische Instanz eines {@link Telegram}
	 * @throws TelegramParseException wenn bei der Verarbeitung ein Fehler auftritt.
	 */
	public Telegram parse(byte[] rawResponse) throws TelegramParseException {
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
		// -1 B für die Telegrammkennung
        int dataLength = toUInt(rawResponse[T_DATALENPOS]) - 1;
		if (rawResponse.length < T_DATAPOS+dataLength){
			throw new TelegramParseException(String.format("Bytearray ist kürzer (%d) als angegeben (%d).", rawResponse.length, T_DATAPOS+dataLength));
		}
		byte[] data = Arrays.copyOfRange(rawResponse, T_DATAPOS, T_DATAPOS+dataLength);
		
		//Todo: add real parser logic
        //Todo: response is 0000000... if connection ended

		// Telegramm-Kategorien
		switch (rawResponse[T_CATPOS]) {
			case B_TC_LABTIME: return parseLabTimeData(data);
			case B_TC_TRAINRT:
				// Zuglaufende
				if (data[0] == B_TC_TRENDMK) {
					return parseTrainRouteEndData(data);
				}
				return parseTrainRouteData(data);
			
			case B_TC_STATION: return parseStationNameData(data);
			
			default:
				throw new TelegramParseException(String.format("Unbekannte Kategorie: %0#4x", rawResponse[T_CATPOS]));
		}
	}
	
	/**
	 * Verarbeitet ein Laborzeittelegramm.
	 * 
	 * <p> Die Daten liegen in folgender Form vor:
	 * Stunde[Byte], Minute[Byte], Sekunde[Byte]
	 * darauf folgen die nicht verarbeiteten Werte für den Laboruhr-Faktor[Byte]
	 * und Züge Stopp/Weiter[Byte]
	 * 
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
			t = LocalTime.of(toUInt(rawData[0]), toUInt(rawData[1]), toUInt(rawData[2]));
		} catch (DateTimeException e){
			throw new TelegramParseException("Fehler beim Verarbeiten der Zeitangaben: "+e);
		}

		LabTimeTelegram returnTele = new LabTimeTelegram(t);
		LOGGER.debug("Parsed " + returnTele);
		return returnTele;
	}
	
	/**
	 * Verarbeitet Zuglauftelegeramme.
	 * 
	 * <p> Die Daten liegen in folgender Form vor:
	 * Fahrplannummer[B], Länge Zugnummer[B], Zugnummer[6B],
	 * Übergang in Zugnummer[5B], Länge Gattung[B], Gattung[6B],
	 * TFz1[B], ZMA[4B], Meldungsindex[B], Anzahl Halte[B]
	 * 
	 * <p> Darauf folgt eine Liste mit Halten, wobei die Daten für jeden in
	 * folgender Form vorliegen:
	 * Bahnhofs-ID[B], Ankunft-Soll[2B], Abfahrt-Soll[2B], Ankunft-Verspätung[B],
	 * Abfahrt-Verspätung[B], Gleis-Soll[B], Gleis-Ist[B], Dispo-Typ[B],
	 * Meldungs-ID[B]
	 * 
	 * @param rawData
	 * @return
	 * @throws TelegramParseException 
	 */
	private TrainRouteTelegram parseTrainRouteData(byte[] rawData) throws TelegramParseException{
		/* Header-Bereich */
		final int HEADER_MIN_LEN	= 27;
		// Zugnummer [2-7]
		final int TRNNUM_POS		= 2;
		final int TRNNUM_LEN_POS	= 1;
		final int TRNNUM_MAX_LEN	= 6;
		// Zug-Kategorie [14-19]
		final int TRNCAT_POS		= 14;
		final int TRNCAT_LEN_POS	= 13;
		final int TRNCAT_MAX_LEN	= 6;
		// Meldung
		final int MESGID_POS		= 25;
		// Anzahl Halte
		final int STOP_COUNT_POS	= 26;
		final int STOP_COUNT_MAX	= 10;
		
		/* einzelner Halt */
		final int STPDAT_MIN_LEN	= 13;
		final int STPDAT_STN_POS	= 0;	// Bahnhofs-ID
		final int STPDAT_ARR_POS	= 1;	// Ankunft [2B]
		final int STPDAT_DEP_POS	= 3;	// Abfahrt [2B]
		final int STPDAT_ADL_POS	= 5;	// Änderung zur Ankunftszeit [2B]
		final int STPDAT_DDL_POS	= 7;	// Änderung zur Abfahrtszeit [2B]
		final int STPDAT_TRK_POS	= 9;	// Gleis geplant TODO: UInt?
		final int STPDAT_NTR_POS	= 10;	// neues Gleis
		final int STPDAT_DPT_POS	= 11;	// DispoTyp TODO: ?
		final int STPDAT_MSG_POS	= 12;	// Meldungs-Index
		
		// Telegrammlänge
		if (rawData.length < HEADER_MIN_LEN){
			throw new TelegramParseException(String.format("Zuglauftelegramm kürzer (%d) als erwartet (%d)", rawData.length, HEADER_MIN_LEN));
		}
		
		// Zugnummer
		int trnNumLen = toUInt(rawData[TRNNUM_LEN_POS]);
		if (trnNumLen == 0){
			throw new TelegramParseException("Eine leere Zugnummer ist nicht möglich!");
		}
		if (trnNumLen > TRNNUM_MAX_LEN){
			throw new TelegramParseException(String.format("Zugnummer länger (%d) als erwartet (%d)", trnNumLen, TRNNUM_MAX_LEN));
		}
		String trnNum = new String(Arrays.copyOfRange(rawData, TRNNUM_POS, TRNNUM_POS+trnNumLen), CHARSET);
		
		// Zugkategorie
		int trnCatLen = toUInt(rawData[TRNCAT_LEN_POS]);
		if (trnCatLen == 0){
			throw new TelegramParseException("Eine leere Zugkategorie ist nicht möglich!");
		}
		if (trnCatLen > TRNCAT_MAX_LEN){
			throw new TelegramParseException(String.format("Zugkategorie länger (%d) als erwartet (%d)", trnCatLen, TRNCAT_MAX_LEN));
		}
		String trnCat = new String(Arrays.copyOfRange(rawData, TRNCAT_POS, TRNCAT_POS+trnCatLen), CHARSET);
		
		// Meldung
		int messageID = toUInt(rawData[MESGID_POS]);
		
		// Halte
		int stopCount = toUInt(rawData[STOP_COUNT_POS]);
		int stopEndPos = HEADER_MIN_LEN + stopCount*STPDAT_MIN_LEN;
		if (stopCount > STOP_COUNT_MAX){
			throw new TelegramParseException(String.format("Zuglauf enthält mehr Halte (%d) als maximal erwartet (%d)", stopCount, STOP_COUNT_MAX));
		}
		if (rawData.length < stopEndPos){
			throw new TelegramParseException(String.format("Zuglaufdaten sind kürzer (%d) als angegeben (%d)", rawData.length, stopEndPos));
		}
		
		List<TrainRouteTelegram.StopData> stops = new ArrayList<>();
		TrainRouteTelegram telegram = new TrainRouteTelegram(
			new TrainRouteTelegram.TrainRouteData(trnNum, trnCat, messageID, stops)
		);
		
		// einzeln durchlaufen
		// TODO handle null values
		byte[] stopData;
		for (int i = HEADER_MIN_LEN; i < stopEndPos; i += STPDAT_MIN_LEN){
			stopData = Arrays.copyOfRange(rawData, i, i + STPDAT_MIN_LEN);
			
			int station = toUInt(stopData[STPDAT_STN_POS]);
			
			LocalTime sArrival = fromTenthOfMinute(toUInt(stopData[STPDAT_ARR_POS], stopData[STPDAT_ARR_POS+1], LITTLE_ENDIAN));
			LocalTime sDeparture = fromTenthOfMinute(toUInt(stopData[STPDAT_DEP_POS], stopData[STPDAT_DEP_POS+1], LITTLE_ENDIAN));
			LocalTime aArrival = fromTenthOfMinute(toInt(stopData[STPDAT_ADL_POS], stopData[STPDAT_ADL_POS+1], LITTLE_ENDIAN), sArrival);
			LocalTime aDeparture = fromTenthOfMinute(toInt(stopData[STPDAT_DDL_POS], stopData[STPDAT_DDL_POS+1], LITTLE_ENDIAN), sDeparture);
			
			int sTrack = toUInt(stopData[STPDAT_TRK_POS]);
			int aTrack = toUInt(stopData[STPDAT_NTR_POS]);
			int dispoId = toUInt(stopData[STPDAT_DPT_POS]);
			int messageId = toUInt(stopData[STPDAT_MSG_POS]);
			
			stops.add(new TrainRouteTelegram.StopData(station, sArrival, sDeparture, aArrival, aDeparture, sTrack, aTrack, dispoId, messageId));
		}

		LOGGER.debug("Parsed " + telegram);
		return telegram;
	}
	
	private TrainRouteEndTelegram parseTrainRouteEndData(byte[] data){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Verarbeitet ein Betriebsstellen-Bezeichnungstelegramm.
	 * 
	 * <p> Die Daten liegen in folgender Form vor:
	 * ID[Byte], YY = Länge der Abkürzung[Byte], Abkürzung[YY Bytes cp1252]
	 * Bezeichnung[Byte[] cp1252]
	 * 
	 * @param rawData
	 * @return
	 * @throws TelegramParseException 
	 */
	private StationNameTelegram parseStationNameData(byte[] rawData) throws TelegramParseException{
		final int MIN_LENGTH = 4;
		
		if(rawData.length < MIN_LENGTH) {
			throw new TelegramParseException(String.format("Bytearray für BS-Bezeichnungstelegramm ist kürzer (%d) als erwartet (%d)", rawData.length, MIN_LENGTH));
		}
		
		final int codeLength = toUInt(rawData[1]);
		final String code = new String(Arrays.copyOfRange(rawData, 2, codeLength+2), CHARSET);
		final String name = new String(Arrays.copyOfRange(rawData, codeLength+2, rawData.length), CHARSET);
		
		// TODO coords
		
		StationNameTelegram returnTele = new StationNameTelegram(rawData[0], code, name);
		LOGGER.debug("Parsed " + returnTele);
		return returnTele;
	}
}
