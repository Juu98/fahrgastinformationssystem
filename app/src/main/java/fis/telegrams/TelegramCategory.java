package fis.telegrams;

/**
 * Konstanten zur Identifikation der Telegrammkategorie
 * @author Robert
 */
public enum TelegramCategory {
	// Telegrammkategorie-Konstanten (Byte 5 im Telegramm)
	REGISTRATION (251),	// 0xFB
	LABTIME (241),		// 0xF1
	CLIENTSTATUS (243),	// 0xF3
	TRAINROUTE (236),	// 0xEC
	STATIONNAME (238);	// 0xEE
	
	private final int mark;
	TelegramCategory(int mark){
		this.mark = mark;
	}
	
	/**
	 * Liefert den Identifikator als Byte
	 * @return das Byte
	 */
	public byte value(){
		return (byte) (this.mark & 0xFF);
	}
	
	/**
	 * Wandelt ein Byte in die zugehörige Kategorie um.
	 * @param mark das identifizierende Byte
	 * @return die Kategorie oder {@literal null}, falls diese nicht vorhanden ist.
	 */
	public static TelegramCategory fromByte(byte mark){
		for (TelegramCategory t : TelegramCategory.values()){
			if (t.value() == mark){
				return t;
			}
		}
		
		return null;
	}
}
