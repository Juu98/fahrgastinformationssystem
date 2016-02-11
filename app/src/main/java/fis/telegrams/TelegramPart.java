/* Copyright 2016 Erik Schölzel, Robert Mörseburg, Zdravko Yanakiev, Jonas Schenke, Oliver Schmidt
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

/**
 * Teile eines Telegramms.
 * Jeder Part beginnt an einem festen Index im Telegramm und hat eine
 * festgelegte Länge und ggf. einen vorgeschriebenen Wert.
 *
 * @author Robert
 */
public enum TelegramPart {
	/**
	 * Das komplette Bytearray
	 */
	RAW_DATA(0, 255, null),
	/**
	 * Die Startkennung
	 */
	START(0, 3, 255),
	/**
	 * Die Längenangabe der Nutzdaten inklusive Kategorie-Kennung
	 */
	DATA_LENGTH(START.maxLength(), 1, null),
	/**
	 * Die Kategorie-Kennung
	 */
	CATEGORY(START.maxLength() + 1, 1, 0),
	/**
	 * Die eigentlichen Nutzdaten
	 */
	DATA(START.maxLength() + 2, 251, null);

	private final int start;
	private final int maxLength;
	private final Integer value;

	TelegramPart(int start, int maxLength, Integer value) {
		this.start = start;
		this.maxLength = maxLength;
		this.value = value;
	}

	/**
	 * Liefert die Anfangsposition des Telegrammteils
	 *
	 * @return positive Zahl, erste Stelle hat die Position {@literal 0}
	 */
	public int start() {
		return this.start;
	}

	/**
	 * Liefert die maximale Länge des Telegrammteils.
	 *
	 * @return positive Zahl
	 */
	public int maxLength() {
		return this.maxLength;
	}

	/**
	 * Liefert den Standardwert für diesen Telegrammteil.
	 *
	 * @return den Wert oder {@literal null}, wenn keiner gesetzt wurde
	 */
	public Byte value() {
		if (this.value == null) {
			return null;
		}
		return (byte) (this.value & 0xFF);
	}
}
