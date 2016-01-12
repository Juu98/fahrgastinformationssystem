package fis.telegrams;

import java.util.Arrays;

/**
 * Eine Klasse für die Anmeldetelegramme.
 *
 * @author spiollinux, kloppstock, Robert
 */
public class RegistrationTelegram extends SendableTelegram {
	private final byte id;
	public static final int LABTIME_FACTOR = 1;
	public static final int TRAINS_STOP_GO = 1;

	/**
	 * Konstruktor für die Anmeldetelegramme.
	 *
	 * @param id die Client-ID, welche an den Server gesendet werden soll
	 */
	public RegistrationTelegram(byte id) {
		super();

		this.id = id;
		byte[] data = new byte[3];
		// Client-ID
		data[0] = this.id;
		// Laborzeit-Faktor
		data[1] = ByteConversions.toUByte(LABTIME_FACTOR);
		// Züge Stop/Weiter
		data[2] = ByteConversions.toUByte(TRAINS_STOP_GO);

		this.setCategory(TelegramCategory.REGISTRATION);
		this.setData(data);
	}

	@Override
	public String toString() {
		return String.format("RegistrationTelegram: ID %02x", this.id);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!other.getClass().equals(this.getClass())) {
			return false;
		}
		RegistrationTelegram o = (RegistrationTelegram) other;
		return Arrays.equals(this.getRawTelegram(), o.getRawTelegram());
	}
}
