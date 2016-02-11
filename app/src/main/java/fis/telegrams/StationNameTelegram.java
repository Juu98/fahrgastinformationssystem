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
 * Eine Klasse für Bahnhofsnamentelegramme.
 *
 * @author spiollinux, kloppstock
 */
public class StationNameTelegram extends Telegram {
	private byte id;
	private String code;
	private String name;
	private float x;
	private float y;

	/**
	 * Konstruktor für Bahnhofsnamentelegramme.
	 *
	 * @param ID
	 * @param code
	 * @param name
	 * @throws IllegalArgumentException
	 */
	public StationNameTelegram(byte ID, String code, String name, float x, float y) throws IllegalArgumentException {
		if (code == null || name == null)
			throw new IllegalArgumentException("Name und Abkürzung dürfen nicht null sein.");
		this.id = ID;
		this.code = code;
		this.name = name;
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter für ID.
	 *
	 * @return ID
	 */
	public byte getId() {
		return this.id;
	}

	/**
	 * Getter für code.
	 *
	 * @return code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * Getter für name.
	 *
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}


	@Override
	public String toString() {
		return String.format("StationNameTelegram: ID %0#4x; [%s] %s", this.id, this.code, this.name);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!other.getClass().equals(this.getClass())) {
			return false;
		}
		StationNameTelegram o = (StationNameTelegram) other;
		return
				this.id == o.getId() &&
						this.code.equals(o.getCode()) &&
						this.name.equals(o.getName());
	}
}
