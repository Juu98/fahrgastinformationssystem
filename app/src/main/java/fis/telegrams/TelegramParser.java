package fis.telegrams;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Verarbeitet die rohen Telegrammdaten.
 * <p>
 * <p> Telegramme sind nach folgender Struktur aufgebaut:
 * 0 - 2: {@literal FF FF FF} Startkennung,
 * 3: {@literal XX} Länge der Nutzdaten inklusive Kennung
 * 4: {@literal TT} Kennung des Telegrammtyps
 * 5- XX+5: Nutzdaten (max. 251 B lang)
 *
 * @author spiollinux, Robert
 */
@Component
public class TelegramParser {
	private static final Logger LOGGER = Logger.getLogger(TelegramParser.class);

	/**
	 * Übersetzt ein empfangenes Bytearray in ein Telegramm.
	 *
	 * @param rawResponse empfangene Bytefolge mit maximaler Länge 255
	 * @return spezifische Instanz eines {@link Telegram}
	 * @throws TelegramParseException wenn bei der Verarbeitung ein Fehler auftritt.
	 */
	public Telegram parse(byte[] rawResponse) throws TelegramParseException {
		// Telegrammlänge prüfen
		if (rawResponse == null) {
			throw new TelegramParseException("Versuch 'null' zu parsen.");
		} else if (rawResponse.length == 0) {
			throw new TelegramParseException("Versuch ein leeres Bytearray zu parsen.");
		} else if (rawResponse.length < TelegramPart.DATA.start()) {
			throw new TelegramParseException(String.format("Bytearray ist kürzer (%d B) als erwartet (%d B).", rawResponse.length, TelegramPart.DATA.start()));
		} else if (rawResponse.length > TelegramPart.RAW_DATA.maxLength()) {
			throw new TelegramParseException(String.format("Bytearray ist länger (%d B) als erwartet (%d B).", rawResponse.length, TelegramPart.RAW_DATA.maxLength()));
		}

		// Startkennung
		for (int i = 0; i < TelegramPart.START.maxLength(); i++) {
			if (rawResponse[i] != TelegramPart.START.value()) {
				throw new TelegramParseException(String.format("Byte %d (%0#4x) hat falsches Format, %0#4x erwartet.", i, rawResponse[i], TelegramPart.START.value()));
			}
		}

		// Nutzdaten extrahieren
		// -1 B für die Telegrammkennung
		int dataLength = ByteConversions.toUInt(rawResponse[TelegramPart.DATA_LENGTH.start()]) - 1;
		if (rawResponse.length < TelegramPart.DATA.start() + dataLength) {
			throw new TelegramParseException(String.format("Bytearray ist kürzer (%d) als angegeben (%d).", rawResponse.length, TelegramPart.DATA.start() + dataLength));
		}
		byte[] data = Arrays.copyOfRange(rawResponse, TelegramPart.DATA.start(), TelegramPart.DATA.start() + dataLength);

		// Telegramm-Kategorien
		switch (TelegramCategory.fromByte(rawResponse[TelegramPart.CATEGORY.start()])) {
			case LABTIME:
				return parseLabTimeData(data);
			case TRAINROUTE:
				// Zuglaufende
				if (data[0] == (byte) 0xFF) {
					return parseTrainRouteEndData(data);
				}
				return parseTrainRouteData(data);

			case STATIONNAME:
				return parseStationNameData(data);

			default:
				throw new TelegramParseException(String.format("Unbekannte Kategorie: %0#4x", rawResponse[TelegramPart.CATEGORY.start()]));
		}
	}

	/**
	 * Verarbeitet ein Laborzeittelegramm.
	 * <p>
	 * <p> Die Daten liegen in folgender Form vor:
	 * Stunde[Byte], Minute[Byte], Sekunde[Byte]
	 * darauf folgen die nicht verarbeiteten Werte für den Laboruhr-Faktor[Byte]
	 * und Züge Stopp/Weiter[Byte]
	 *
	 * @param rawData die empfangenen Rohdaten, mindestens 3 Bytes
	 * @return ein fertiges Laborzeittelegramm
	 * @throws TelegramParseException
	 */
	private LabTimeTelegram parseLabTimeData(byte[] rawData) throws TelegramParseException {
		// mindestens drei Byte (h, min, sec)
		final int MIN_LENGTH = 3;
		if (rawData.length < MIN_LENGTH) {
			throw new TelegramParseException(String.format("Bytearray für Laborzeittelegramm ist kürzer (%d) als erwartet (%d)", rawData.length, MIN_LENGTH));
		}

		// Werte auslesen
		LocalTime t;
		try {
			t = LocalTime.of(ByteConversions.toUInt(rawData[0]), ByteConversions.toUInt(rawData[1]), ByteConversions.toUInt(rawData[2]));
		} catch (DateTimeException e) {
			throw new TelegramParseException("Fehler beim Verarbeiten der Zeitangaben: " + e);
		}

		LabTimeTelegram returnTele = new LabTimeTelegram(t);
		LOGGER.debug("Parsed " + returnTele);
		return returnTele;
	}

	/**
	 * Verarbeitet Zuglauftelegeramme.
	 * <p>
	 * <p> Die Daten liegen in folgender Form vor:
	 * Fahrplannummer[B], Länge Zugnummer[B], Zugnummer[6B],
	 * Übergang in Zugnummer[5B], Länge Gattung[B], Gattung[6B],
	 * TFz1[B], ZMA[4B], Meldungsindex[B], Anzahl Halte[B]
	 * <p>
	 * <p> Darauf folgt eine Liste mit Halten, wobei die Daten für jeden in
	 * folgender Form vorliegen:
	 * Bahnhofs-ID[B], Ankunft-Soll[2B], Abfahrt-Soll[2B], Ankunft-Verspätung[B],
	 * Abfahrt-Verspätung[B], Gleis-Soll[B], Gleis-Ist[B], Dispo-Typ[B],
	 * Meldungs-ID[B]
	 *
	 * @param rawData die empfangenen Rohdaten
	 * @return das verarbeitete Telegramm
	 * @throws TelegramParseException
	 */
	private TrainRouteTelegram parseTrainRouteData(byte[] rawData) throws TelegramParseException {
		// Zugnummer
		final int TRNNUM_OFFSET = 1;
		// Zug-Kategorie
		final int TRNCAT_OFFSET = 4;
		// Meldung
		final int MESGID_OFFSET = 5;
		// Anzahl Halte
		final int STOP_COUNT_OFFSET = 0;
		final int STOP_COUNT_MAX = 10;
		
		/* einzelner Halt */
		final int STPDAT_MIN_LEN = 13;
		final int STPDAT_STN_POS = 0;    // Bahnhofs-ID
		final int STPDAT_ARR_POS = 1;    // Ankunft [2B]
		final int STPDAT_DEP_POS = 3;    // Abfahrt [2B]
		final int STPDAT_ADL_POS = 5;    // Änderung zur Ankunftszeit [2B]
		final int STPDAT_DDL_POS = 7;    // Änderung zur Abfahrtszeit [2B]
		final int STPDAT_TRK_POS = 9;    // Gleis geplant
		final int STPDAT_NTR_POS = 10;    // neues Gleis
		final int STPDAT_DPT_POS = 11;    // DispoTyp - ignorieren
		final int STPDAT_MSG_POS = 12;    // Meldungs-Index
		/* Sonderwerte */
		final int STPDAT_NUL_VAL = 44444;
		final int STPDAT_PAS_VAL = 55555;

		// TODO: Länge prüfen

		// Index zum Durchlaufen
		int i = TRNNUM_OFFSET;

		// Zugnummer
		int trnNumLen = ByteConversions.toUInt(rawData[i]);
		i++;

		if (trnNumLen == 0) {
			throw new TelegramParseException("Eine leere Zugnummer ist nicht möglich!");
		}
		String trnNum = new String(Arrays.copyOfRange(rawData, i, i + trnNumLen), Telegram.CHARSET);
		i += trnNumLen + TRNCAT_OFFSET;

		// Zugkategorie
		int trnCatLen = ByteConversions.toUInt(rawData[i]);
		i++;

		if (trnCatLen == 0) {
			throw new TelegramParseException("Eine leere Zugkategorie ist nicht möglich!");
		}
		String trnCat = new String(Arrays.copyOfRange(rawData, i, i + trnCatLen), Telegram.CHARSET);
		i += trnCatLen + MESGID_OFFSET;

		// Meldung
		int messageID = ByteConversions.toUInt(rawData[i]);
		i += 1 + STOP_COUNT_OFFSET;

		// Halte
		int stopCount = ByteConversions.toUInt(rawData[i]);
		i++;

		int stopEndPos = i + stopCount * STPDAT_MIN_LEN;
		if (stopCount > STOP_COUNT_MAX) {
			throw new TelegramParseException(String.format("Zuglauf enthält mehr Halte (%d) als maximal erwartet (%d)", stopCount, STOP_COUNT_MAX));
		}
		if (rawData.length < stopEndPos) {
			throw new TelegramParseException(String.format("Zuglaufdaten sind kürzer (%d) als angegeben (%d)", rawData.length, stopEndPos));
		}

		List<TrainRouteTelegram.StopData> stops = new ArrayList<>(stopCount);
		TrainRouteTelegram telegram = new TrainRouteTelegram(
				new TrainRouteTelegram.TrainRouteData(trnNum, trnCat, messageID, stops)
		);

		// einzeln durchlaufen
		byte[] stopData;
		int station, arrTenth, depTenth;
		LocalTime aArrival, sArrival, aDeparture, sDeparture;
		int sTrack, aTrack, dispoId, messageId;
		while (i < stopEndPos) {
			stopData = Arrays.copyOfRange(rawData, i, i + STPDAT_MIN_LEN);
			station = ByteConversions.toUInt(stopData[STPDAT_STN_POS]);

			// Zeiten
			sArrival = null;
			sDeparture = null;
			aArrival = null;
			aDeparture = null;

			arrTenth = ByteConversions.toUInt(stopData[STPDAT_ARR_POS], stopData[STPDAT_ARR_POS + 1], Telegram.LITTLE_ENDIAN);
			if (arrTenth != STPDAT_NUL_VAL && arrTenth != STPDAT_PAS_VAL) {
				sArrival = ByteConversions.fromTenthOfMinute(arrTenth);
				aArrival = ByteConversions.fromTenthOfMinute(ByteConversions.toInt(stopData[STPDAT_ADL_POS], stopData[STPDAT_ADL_POS + 1], Telegram.LITTLE_ENDIAN), sArrival);
			}
			depTenth = ByteConversions.toUInt(stopData[STPDAT_DEP_POS], stopData[STPDAT_DEP_POS + 1], Telegram.LITTLE_ENDIAN);
			if (depTenth != STPDAT_NUL_VAL && depTenth != STPDAT_PAS_VAL) {
				sDeparture = ByteConversions.fromTenthOfMinute(depTenth);
				aDeparture = ByteConversions.fromTenthOfMinute(ByteConversions.toInt(stopData[STPDAT_DDL_POS], stopData[STPDAT_DDL_POS + 1], Telegram.LITTLE_ENDIAN), sDeparture);
			}

			sTrack = ByteConversions.toUInt(stopData[STPDAT_TRK_POS]);
			aTrack = ByteConversions.toUInt(stopData[STPDAT_NTR_POS]);
			dispoId = ByteConversions.toUInt(stopData[STPDAT_DPT_POS]);
			messageId = ByteConversions.toUInt(stopData[STPDAT_MSG_POS]);

			// Filter PASSES heraus
			// null-Werte werden weiterhin gespeichert
			if (arrTenth != STPDAT_PAS_VAL){
				stops.add(new TrainRouteTelegram.StopData(station, sArrival, sDeparture, aArrival, aDeparture, sTrack, aTrack, dispoId, messageId));
			}
			i += STPDAT_MIN_LEN;
		}

		LOGGER.debug("Parsed " + telegram);
		return telegram;
	}

	/**
	 * Verarbeitet Zuglaufendetelegramme.
	 *
	 * @param data die empfangenen Rohdaten
	 * @return das verarbeitete Telegramm
	 */
	private TrainRouteEndTelegram parseTrainRouteEndData(byte[] data) {
		TrainRouteEndTelegram telegram = new TrainRouteEndTelegram();
		LOGGER.debug("Received " + telegram);
		return telegram;
	}

	/**
	 * Verarbeitet ein Betriebsstellen-Bezeichnungstelegramm.
	 * <p>
	 * <p> Die Daten liegen in folgender Form vor:
	 * ID[Byte], YY = Länge der Abkürzung[Byte], Abkürzung[YY Bytes cp1252]
	 * Bezeichnung[Byte[] cp1252]
	 *
	 * @param rawData die empfangenen Rohdaten
	 * @return das verarbeitete Telegramm
	 * @throws TelegramParseException
	 */
	private StationNameTelegram parseStationNameData(byte[] rawData) throws TelegramParseException {
		final int MIN_LENGTH = 12;

		if (rawData.length < MIN_LENGTH) {
			throw new TelegramParseException(String.format("Bytearray für BS-Bezeichnungstelegramm ist kürzer (%d) als erwartet (%d)", rawData.length, MIN_LENGTH));
		}

		final int codeLength = ByteConversions.toUInt(rawData[1]);
		final String code = new String(Arrays.copyOfRange(rawData, 2, codeLength + 2), Telegram.CHARSET);
		final String name = new String(Arrays.copyOfRange(rawData, codeLength + 2, rawData.length - 8), Telegram.CHARSET);
		float x = ByteConversions.toFloat(Arrays.copyOfRange(rawData, rawData.length - 8, rawData.length - 4), Telegram.LITTLE_ENDIAN);
		float y = ByteConversions.toFloat(Arrays.copyOfRange(rawData, rawData.length - 4, rawData.length), Telegram.LITTLE_ENDIAN);

		// TODO coords

		StationNameTelegram returnTele = new StationNameTelegram(rawData[0], code, name, x, y);
		LOGGER.debug("Parsed " + returnTele);
		return returnTele;
	}
}
