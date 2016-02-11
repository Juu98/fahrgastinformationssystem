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

import java.time.LocalTime;

/**
 * @author spiollinux, kloppstock
 *         Eine Klasse für Laborzeittelegramme.
 */
public class LabTimeTelegram extends Telegram {
	private LocalTime time;

	/**
	 * Konstruktor für das Laborzeittelegramm.
	 *
	 * @param time (Zeit)
	 * @throws IllegalArgumentException
	 */
	public LabTimeTelegram(LocalTime time) throws IllegalArgumentException {
		if (time == null)
			throw new IllegalArgumentException();
		this.time = time;
	}

	/**
	 * Getter für time.
	 *
	 * @return time (Zeit)
	 */
	public LocalTime getTime() {
		return this.time;
	}

	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!other.getClass().equals(this.getClass())) {
			return false;
		}

		LabTimeTelegram o = (LabTimeTelegram) other;
		return this.time.equals(o.getTime());
	}

	@Override
	public String toString() {
		return "Laborzeittelegramm: " + this.time.toString();
	}
}
