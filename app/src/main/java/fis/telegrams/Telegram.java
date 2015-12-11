package fis.telegrams;

import java.time.LocalTime;

/**
 * Created by spiollinux on 12.11.15.
 */
public abstract class Telegram {
	protected static final int rawTelegramLength = 255;

	public static int getRawTelegramLength() {
		return rawTelegramLength;
	}
	public static Telegram parse(byte[] rawResponse) throws TelegramParseException {
		Telegram returnTelegram = null;
		//Todo: add real parser logic
        //Todo: response is 0000000... if connection ended
        for (int i = 0; i < 3; ++i) {
            if (rawResponse[i] != (byte) 0xFF) {
                throw (new TelegramParseException("Byte " + i + " hat falsches Format: " + rawResponse[i]));
            }
        }
        int messageLength = (rawResponse[3] & 0xFF);

		switch (rawResponse[4]) {
			case (byte) (241 & 0xFF):
				if(messageLength < 6) {
					throw new TelegramParseException("Laborzeittelegramm too short");
				}
				int hour = ((int) rawResponse[5]) & 0xFF;
				int minute = ((int) rawResponse[6]) & 0xFF;
				int second = ((int) rawResponse[7]) & 0xFF;
				returnTelegram = new LabTimeTelegram(LocalTime.of(hour, minute, second));
				break;
		}

		if(returnTelegram == null) {
			throw new TelegramParseException("received telegram has unknown type byte");
		}
		return returnTelegram;
	}
}
