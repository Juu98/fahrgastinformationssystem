package fis.telegrams;

/**
 * Created by spiollinux on 12.11.15.
 */
public abstract class Telegram {
	protected static final int rawTelegramLength = 255;

	public static int getRawTelegramLength() {
		return rawTelegramLength;
	}
	public static Telegram parse(byte[] rawResponse) throws TelegramParseException {
		//Todo: add real parser logic
        //Todo: response is 0000000... if connection ended
        for (int i = 0; i < 3; ++i) {
            if (rawResponse[i] != (byte) 0xFF) {
                throw (new TelegramParseException("Byte " + i + " hat falsches Format: " + rawResponse[i]));
            }
        }
        int messageLength = rawResponse[3];
        for (int i = 4; i < 3 + messageLength; ++i) {
            if (i == 4) {
                //Typangabe
            }
            System.out.println("Byte " + i + ": " + rawResponse[5]);
        }
		return null;
	}
}
