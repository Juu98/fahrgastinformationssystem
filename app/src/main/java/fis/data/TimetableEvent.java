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

import org.springframework.context.ApplicationEvent;

/**
 * Event, das auf notwendige Änderungen der Fahrplan-Datenstruktur oder Datenquelle hinweist
 */
public class TimetableEvent extends ApplicationEvent {
	private TimetableEventType type;

	/**
	 * erstellt neues TimeTableEvent mit einem Typen {@link TimetableEventType}
	 *
	 * @param type
	 */
	public TimetableEvent(TimetableEventType type) {
		super(type);
		this.type = type;
	}

	/**
	 * Gibt den Eventtyp zurück
	 *
	 * @return TimeTableEventType
	 */
	public TimetableEventType getType() {
		return type;
	}

	@Override
	public boolean equals(Object e) {
		if (e.getClass() == TimetableEvent.class) {
			return this.type.equals(((TimetableEvent) e).getType());
		}
		return false;
	}
}
