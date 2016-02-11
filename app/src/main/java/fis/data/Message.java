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
package fis.data;

import java.io.Serializable;

/**
 * Eine Klasse für mögliche Nachrichten, die bei Stops angezeigt werden können.
 *
 * @author kloppstock
 */
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private int index;
	private String message;

	/**
	 * Getter für index.
	 *
	 * @return index
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * Getter für message.
	 *
	 * @return message (Nachricht)
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Setter für index.
	 *
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Setter für message.
	 *
	 * @param message (Nachricht)
	 * @throws IllegalArgumentException
	 */
	public void setMessage(String message) throws IllegalArgumentException {
		if (message == null)
			throw new IllegalArgumentException();
		this.message = message;
	}

	@Override
	public String toString() {
		return this.getIndex() + ": " + ((this.getMessage() == null) ? "" : this.getMessage());
	}
}
