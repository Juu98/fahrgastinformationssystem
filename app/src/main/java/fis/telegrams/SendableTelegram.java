package fis.telegrams;

import java.util.Arrays;

/**
 * Abstrakte Klasse, die die grobe Telegrammstruktur für zu sendene Telegramme
 * implementiert.
 * 
 * @author spiollinux, Robert
 */
public abstract class SendableTelegram extends Telegram {
	private byte[] rawTelegram;
	private int length;

	/**
	 * Standard-Konstruktor. Erstellt ein neues Telegramm mit der Sanardstruktur.
	 */
	public SendableTelegram() {
		// neues Bytearray anlegen
		this.rawTelegram = new byte[TelegramPart.RAW_DATA.maxLength()];
		Arrays.fill(this.rawTelegram, (byte) 0);
						
		// Startkennung
		Arrays.fill(this.rawTelegram,TelegramPart.START.start(),
				TelegramPart.START.maxLength(),
				TelegramPart.START.value());
		
		this.resetLength();
	}
	
	/**
	 * Setz die Kategorie dieses Telegramms.
	 * @param tc die Kategorie
	 */
	public void setCategory(TelegramCategory tc){
		this.rawTelegram[TelegramPart.CATEGORY.start()] =
				(tc == null) ? TelegramPart.CATEGORY.value() : tc.value();
	}
	
	/**
	 * Setzt die Länge des Telegramm auf den Minimalwert zurück
	 */
	private void resetLength(){
		// Gesamtlänge
		this.length = TelegramPart.START.maxLength() +
				TelegramPart.CATEGORY.maxLength() +
				TelegramPart.DATA_LENGTH.maxLength();
	}
	
	/**
	 * Schreibt die Länge der Nutzdaten in das Telegeramm.
	 * @param length neue Länge der Nutzdaten (ohne Kennung)
	 */
	private void updateLength(int length){
		// Gesamtlänge aktualisieren
		this.resetLength();
		this.length += length;
		// Länge Nutzdaten + Kennung in Telegramm schreiben
		this.rawTelegram[TelegramPart.DATA_LENGTH.start()] = ByteConversions.toUByte(
				TelegramPart.CATEGORY.maxLength() + length);
	}
	
	/**
	 * Schreibt den Daten-Teil des Telegramms.
	 * @param data die zu sendende Daten
	 */
	public void setData(byte[] data){
		// Länge nochmal checken
		if (data.length > TelegramPart.DATA.maxLength()){
			throw new IllegalArgumentException(
					String.format("Daten zu lang (%d, erlaubt %d)!",
					data.length, TelegramPart.DATA.maxLength())
			);
		}
		
		// Daten kopieren
		System.arraycopy(data, 0, this.rawTelegram, TelegramPart.DATA.start(), data.length);
		
		// Länge setzen
		this.updateLength(data.length);
	}
	
	/**
	 * Liefert das fertige Telegramm.
	 * @return komplettes ytearray für das Telegramm
	 */
	public byte[] getRawTelegram(){
		return Arrays.copyOfRange(this.rawTelegram, 0, this.length);
	}
}
