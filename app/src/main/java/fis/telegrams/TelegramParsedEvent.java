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

import org.springframework.context.ApplicationEvent;

/**
 * Event, das nach dem erfolgreichen Parsen eines Telegramms ausgelöst wird. Kapselt ein {@see Telegram} Objekt
 */
public class TelegramParsedEvent extends ApplicationEvent {

	/**
	 * Erstellt ein neues TelegramParsedEvent
	 *
	 * @param source ein Telegram Objekt, das mit diesem Event übermittelt werden soll
	 */
	public TelegramParsedEvent(Telegram source) {
		super(source);
		this.source = source;
	}

	public Telegram getSource() {
		return (Telegram) this.source;
	}
}
