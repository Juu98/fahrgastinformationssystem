package fis.telegrams;

import java.io.UnsupportedEncodingException;

/**
 * Eine Klasse für das Clientstatustelegramm. 
 * @author spiollinux, kloppstock
 */
public class ClientStatusTelegram extends Telegram implements SendableTelegram{
	private final byte[] rawTelegram;
	private final String id;
	private final byte status;

	/**
	 * Konstruktor für das Clientstatustelegramm. 
	 * @param ID
	 * @param status
	 * @throws NullPointerException
	 * @throws UnsupportedEncodingException 
	 */
	public ClientStatusTelegram(String ID, byte status) throws NullPointerException, UnsupportedEncodingException {
		if(ID == null){
			throw new NullPointerException();
		}
		this.id = ID;
		this.status = status;
		
		this.rawTelegram = new byte[Telegram.rawTelegramLength];
		int i = 0;
		//Laut Spezifikation müssen die ersten 3 Telegrammbytes auf 0xFF stehen
		for(; i < 3; i++)
			this.rawTelegram[i] = (byte) 255;
		//Das nächste Byte gibt die Länge des genutzten Bereiches an.
		//Dieses Byte wird erst am Ende gesetzt. 
		i++;
		//Das 5. Byte beinhaltet die Kennung, welche für das das Anmeldetelegramm laut Spezifikation 251 ist,  
		this.rawTelegram[i++] = (byte) 243;
		//Die nächsten 7 Bytes werden mit dem Bezeichner gefüllt. 
		for(int j = 0; j < 7; j++){
			if(j < ID.length())
				this.rawTelegram[i++] = ID.getBytes("windows-1252")[j];
			else
				this.rawTelegram[i++] = (byte) 0;
		}
		//Das letzte Nutzbyte wird für den Status verwendet.
		this.rawTelegram[i] = status;
		//Die Kennung ist ein Byte lang, gefolgt von 7 Bytes für den Bezeichner und 1 Byte für den Status.  
		this.rawTelegram[3] = (byte) (i-4);
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
		return String.format("ClientStatusTelegram: ID %s; Status %0#4x", this.id, this.status);
	}
}
