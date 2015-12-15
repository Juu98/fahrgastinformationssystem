package fis.telegrams;

import java.util.Arrays;

/**
 * Eine Klasse für die Anmeldetelegramme. 
 * @author spiollinux, kloppstock
 */
public class RegistrationTelegram extends Telegram implements SendableTelegram{
	private final byte[] rawTelegram;
	private final byte id;

	/**
	 * Konstruktor für die Anmeldetelegramme. 
	 * @param id
	 */
	public RegistrationTelegram(byte id) {
		this.id = id;
		this.rawTelegram = new byte[Telegram.rawTelegramLength];
		int i = 0;
		//Laut Spezifikation müssen die ersten 3 Telegrammbytes auf 0xFF stehen
		for(; i < 3; i++)
			this.rawTelegram[i] = (byte) 255;
		//Das nächste Byte gibt die Länge des genutzten Bereiches an.
		//Diesed Byte wird erst am Ende gesetzt.
		i++;
		//Das 5. Byte beinhaltet die Kennung, welche für das das Anmeldetelegramm laut Spezifikation 251 ist,  
		this.rawTelegram[i++] = (byte) 251; 
		//Das 6. Byte enthällt schließlich die ID. 
		this.rawTelegram[i++] = id;
		//Die nächsten beiden Bytes enthalten den Laboruhrzeitfaktor und das Stopp/Start Signal und werden beide auf Standardwerte (siehe Spezifikation) gesetzt.
		this.rawTelegram[i++] = (byte) 1;
		this.rawTelegram[i] = (byte) 1;
		System.err.println(i);
		//Da nur die Kennung und die ID gesendet wird, ist der genutzte Bereich 2 Byte lang. 
		this.rawTelegram[3] = (byte) (i-3);
	}

	/**
	 * Getter für das im Konstruktor generierte "Rohtelegramm". 
	 * @return rawTelegram
	 */
	@Override
	public byte[] getRawTelegram() {
		return this.rawTelegram;
	}

	@Override
	public String toString() {
		String ret = "";
		int l = TelegramParser.toUInt(this.rawTelegram[3]);
		for (int i = 0; i <= l + 3; i++) {
			ret += "" + i + ": " + TelegramParser.toUInt(rawTelegram[i]) + "\n";
		}
		return ret;
		//return String.format("RegistrationTelegram: ID %02x", this.id);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null){
			return false;
		}
		if (!other.getClass().equals(this.getClass())){
			return false;
		}
		RegistrationTelegram o = (RegistrationTelegram) other;
		return Arrays.equals(this.rawTelegram, o.getRawTelegram());
	}
}
