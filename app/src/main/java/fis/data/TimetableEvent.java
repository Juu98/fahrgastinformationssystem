package fis.data;

import org.springframework.context.ApplicationEvent;

/**
 * Event, das auf notwendige Änderungen der Fahrplan-Datenstruktur oder Datenquelle hinweist
 */
public class TimetableEvent extends ApplicationEvent{
	private TimetableEventType type;

	/**
	 * erstellt neues TimeTableEvent mit einem Typen {@link TimetableEventType}
	 * @param type
	 */
	public TimetableEvent(TimetableEventType type) {
		super(type);
		this.type = type;
	}

	/**
	 * Gibt den Eventtyp zurück
	 * @return TimeTableEventType
	 */
	public TimetableEventType getType() {
		return type;
	}

	@Override
	public boolean equals(Object e) {
		if(e.getClass() == TimetableEvent.class) {
			return this.type.equals(((TimetableEvent) e).getType());
		}
		return false;
	}
}
