package fis.telegrams;


/**
 * Created by spiollinux on 12.11.15.
 */
public abstract class Telegram {
	protected static final int rawTelegramLength = 255;

	public static int getRawTelegramLength() {
		return rawTelegramLength;
	}

	@Override
	public abstract String toString();
}
