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
 * Eine Klasse für Zuglaufendtelegramme.
 *
 * @author schmittlauch, kloppstock
 */
public class TrainRouteEndTelegram extends Telegram {

	/**
	 * Konstruktor für Zuglaufendtelegramme.
	 */
	public TrainRouteEndTelegram() {

	}

	@Override
	public String toString() {
		return "TrainRouteEndTelegram";
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		// only check if it's another TrainRouteEndTelegram
		return this.getClass().equals(other.getClass());
	}
}
