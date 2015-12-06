package fis.telegrams;

/**
 * Created by spiollinux on 06.12.15.
 */
public class ClientStatusTelegram implements SendableTelegram{
	private String ID;
	private byte status;

	public ClientStatusTelegram(String ID, byte status) {
		this.ID = ID;
		this.status = status;
	}

	@Override
	public byte[] getRawTelegram() {
		return new byte[255];
	}
}
