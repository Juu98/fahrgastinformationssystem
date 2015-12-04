package fis.telegrams;

/**
 * Created by spiollinux on 12.11.15.
 */
public class RegistrationTelegram extends Telegram implements SendableTelegram {
	byte clientNumber;

	public RegistrationTelegram(byte id) {
		clientNumber = id;
	}

	public byte[] getRawTelegram() {
		return new byte[255];
	}
}
