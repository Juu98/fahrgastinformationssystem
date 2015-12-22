package fis.data;

/**
 * Gibt an, welche Änderungen an der Fahrplanstruktur notwendig sind
 */
public enum TimetableEventType {
	parseRailML,
	/**
	 * alte Datenstruktur aufräumen, sodass sie neu befüllt werden kann (z. B. mit Daten aus Telegrammen)
	 */
	cleanup
}
