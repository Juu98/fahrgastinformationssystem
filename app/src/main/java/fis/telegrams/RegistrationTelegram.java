/* Copyright 2016 Eric Schölzel, Robert Mörseburg, Zdravko Yanakiev, Jonas Schenke, Oliver Schmidt
 *
 * This file is part of FIS.
 *
 * FIS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FIS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FIS.  If not, see <http://www.gnu.org/licenses/>.
 */
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
