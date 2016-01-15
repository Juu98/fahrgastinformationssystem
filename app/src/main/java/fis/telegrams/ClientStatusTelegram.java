package fis.telegrams;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Eine Klasse für das Clientstatustelegramm.
 *
 * @author spiollinux, kloppstock, Robert
 */
public class ClientStatusTelegram extends SendableTelegram {
	private final String id;
	private final byte status;

	public static final int ID_LENGTH = 7;

	/**
	 * Konstruktor für das Clientstatustelegramm.
	 *
	 * @param ID zu sendende ClientID
	 * @param status
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	public ClientStatusTelegram(String ID, byte status) throws UnsupportedEncodingException {
		super();
		byte[] data = new byte[ID_LENGTH + 1];
		Arrays.fill(data, (byte) 0);

		if (ID == null || ID.isEmpty()) {
			throw new IllegalArgumentException("Das ClientStatusTelegramm benötigt eine ID.");
		}
		if (ID.getBytes(Telegram.CHARSET).length > ID_LENGTH) {
			throw new IllegalArgumentException(String.format("Bezeichner zu lang (%d Zeichen).", ID.length()));
		}
		this.id = ID;
		this.status = status;

		// Kategorie setzen
		this.setCategory(TelegramCategory.CLIENTSTATUS);

		// Daten
		System.arraycopy(ID.getBytes(Telegram.CHARSET), 0, data, 0, ID.getBytes(Telegram.CHARSET).length);

		// Status
		data[ID_LENGTH] = status;
		this.setData(data);
	}

	@Override
	public String toString() {
		return String.format("ClientStatusTelegram: ID %s; Status %0#4x", this.id, this.status);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ClientStatusTelegram other = (ClientStatusTelegram) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		if (this.status != other.status) {
			return false;
		}
		return true;
	}
}
