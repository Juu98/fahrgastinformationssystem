package fis.telegrams;

/**
 * Teile eines Telegramms.
 * Jeder Part beginnt an einem festen Index im Telegramm und hat eine
 * festgelegte Länge und ggf. einen vorgeschriebenen Wert.
 * 
 * @author Robert
 */
public enum TelegramPart {
	/** Das komplette Bytearray */
	RAW_DATA (0, 255, null),
	/** Die Startkennung */
	START (0, 3, 255),
	/** Die Längenangabe der Nutzdaten inklusive Kategorie-Kennung */
	DATA_LENGTH (START.maxLength(), 1, null),
	/** Die Kategorie-Kennung */
	CATEGORY (START.maxLength()+1, 1, 0),
	/** Die eigentlichen Nutzdaten */
	DATA (START.maxLength()+2, 251, null);
	
	private final int start;
	private final int maxLength;
	private final Integer value;

	TelegramPart(int start, int maxLength, Integer value){
		this.start = start;
		this.maxLength = maxLength;
		this.value = value;
	}

	/**
	 * Liefert die Anfangsposition des Telegrammteils
	 * @return positive Zahl, erste Stelle hat die Position {@literal 0}
	 */
	public int start(){
		return this.start;
	}

	/**
	 * Liefert die maximale Länge des Telegrammteils.
	 * @return positive Zahl
	 */
	public int maxLength(){
		return this.maxLength;
	}

	/**
	 * Liefert den Standardwert für diesen Telegrammteil.
	 * @return den Wert oder {@literal null}, wenn keiner gesetzt wurde
	 */
	public Byte value(){
		if (this.value == null){
			return null;
		}
		return (byte) (this.value & 0xFF);
	}
}
