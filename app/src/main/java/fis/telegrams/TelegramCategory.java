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

/**
 * Konstanten zur Identifikation der Telegrammkategorie
 *
 * @author Robert
 */
public enum TelegramCategory {
	// Telegrammkategorie-Konstanten (Byte 5 im Telegramm)
	REGISTRATION(251),    // 0xFB
	LABTIME(241),        // 0xF1
	CLIENTSTATUS(243),    // 0xF3
	TRAINROUTE(236),    // 0xEC
	STATIONNAME(238);    // 0xEE

	private final int mark;

	TelegramCategory(int mark) {
		this.mark = mark;
	}

	/**
	 * Liefert den Identifikator als Byte
	 *
	 * @return das Byte
	 */
	public byte value() {
		return (byte) (this.mark & 0xFF);
	}

	/**
	 * Wandelt ein Byte in die zugehörige Kategorie um.
	 *
	 * @param mark das identifizierende Byte
	 * @return die Kategorie oder {@literal null}, falls diese nicht vorhanden ist.
	 */
	public static TelegramCategory fromByte(byte mark) {
		for (TelegramCategory t : TelegramCategory.values()) {
			if (t.value() == mark) {
				return t;
			}
		}

		return null;
	}
}
