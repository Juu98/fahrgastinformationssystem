package fis.telegrams;

import java.nio.charset.Charset;

/**
 * Abstrakte Klasse, die die Telegramme kapselt.
 * 
 * @author spiollinux, Robert
 */
public abstract class Telegram {
	// Codepage für String-Konvertierung
	public static final Charset CHARSET = Charset.forName("ISO-8859-1");
	// Endianness für die Wort-Konvertierung bei Zeiten
	public static final boolean LITTLE_ENDIAN = true;
	
	@Override
	public abstract String toString();
	
	@Override
	public abstract boolean equals(Object other);
}
