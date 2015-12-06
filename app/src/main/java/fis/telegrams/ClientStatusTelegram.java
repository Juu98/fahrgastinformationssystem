package fis.telegrams;

/**
 * Created by spiollinux on 06.12.15.
 */
public class ClientStatusTelegram implements SendableTelegram{
	@Override
	public byte[] getRawTelegram() {
		return new byte[255];
	}
}
