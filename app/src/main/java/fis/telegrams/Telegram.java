package fis.telegrams;


/**
 * Created by spiollinux on 12.11.15.
 */
public abstract class Telegram {
	protected static final int rawTelegramMaxLength = 255;

	public static int getRawTelegramMaxLength() {
		return rawTelegramMaxLength;
	}

	@Override
	public abstract String toString();
	
	@Override
	public abstract boolean equals(Object other);
}
