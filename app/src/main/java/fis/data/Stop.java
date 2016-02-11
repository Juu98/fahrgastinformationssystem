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

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Repräsentiert einen spezifischen Halt
 *
 * @author Eric
 */
public class Stop {
	private Station station;
	private LocalTime scheduledDeparture;
	private LocalTime scheduledArrival;
	private LocalTime actualArrival;
	private LocalTime actualDeparture;

	private String scheduledTrack;
	private String actualTrack;
	private int messageId; 
	private StopType stopType;
	private TrainRoute trainRoute;

	private boolean actualArrivalNextDay = false;
	private boolean actualDepartureNextDay = false;
	
	/**
	 * Erzeugt einen neuen Halt
	 *
	 * @param station            Der Bahnhof, an dem der Zug hält
	 * @param stopType           {@link StopType} dieses Haltes
	 * @param scheduledArrival   Geplante Ankunftszeit des Zuges
	 * @param scheduledDeparture Geplante Abfahrtszeit des Zuges.
	 * @param scheduledTrack     Geplanter Gleis, an dem der Zug halten soll
	 */
	public Stop(Station station, StopType stopType, LocalTime scheduledArrival, LocalTime scheduledDeparture,
	            String scheduledTrack, int messageId) {
		if (station == null || stopType == null)
			throw new IllegalArgumentException();
		this.station = station;
		this.stopType = stopType;
		this.scheduledArrival = scheduledArrival;
		this.scheduledDeparture = scheduledDeparture;
		this.scheduledTrack = scheduledTrack;

		this.actualArrival = scheduledArrival;
		this.actualDeparture = scheduledDeparture;
		this.actualTrack = scheduledTrack;

		this.messageId = messageId;

		station.addStop(this);
	}
	
	/**
	 * Setzt das 'Verspätung der Ankunft zeigt auf den nächsten Tag' - Flag
	 * @param isNextDay
	 */
	public void setActualArrivalNextDay(boolean isNextDay){
		this.actualArrivalNextDay = isNextDay;
	}
	
	/**
	 * Setzt das 'Verspätung der Abfahrt zeigt auf den nächsten Tag' - Flag
	 * @param isNextDay
	 */
	public void setActualDepartureNextDay(boolean isNextDay){
		this.actualDepartureNextDay = isNextDay;
	}

	/**
	 * Getter für station
	 *
	 * @return Den Bahnhof {@link Station}, an dem dieser Halt erfolgt.
	 */
	public Station getStation() {
		return station;
	}

	/**
	 * Setzt den Link auf den Zuglauf {@link TrainRoute}, zu der dieser Halt
	 * gehört.
	 *
	 * @param route Der Zuglauf {@link TrainRoute}, der diesen Halt beinhaltet.
	 */
	public void setTrainRoute(TrainRoute route) {
		this.trainRoute = route;
	}

	/**
	 * Entfernt die Verlinkung dieses Halts zum bisherigen Zuglauf
	 * {@link TrainRoute} Aufrufen, wenn dieser Halt entfernt werden soll (für
	 * Datenkonsistenz). Wird im Normalfall automatisch durch
	 * {@link Station#removeStop(Stop)} oder {@link TrainRoute#removeStops()}
	 * ausgeführt.
	 */
	public void deleteStop() {
		if (trainRoute != null) {
			trainRoute.getStops().remove(this);
		}

		if (station != null) {
			station.getStops().remove(this);
		}

	}

	/**
	 * Getter für stopType
	 * @return {@link StopType} dieses Haltes
	 */
	public StopType getStopType() {
		return stopType;
	}

	/**
	 * Aktualisiert den {@link StopType}
	 *
	 * @param newStopType Der neue {@link StopType}
	 */
	public void updateStopType(StopType newStopType) {
		if (newStopType == null)
			throw new IllegalArgumentException("newStopType darf nicht null sein");
		this.stopType = newStopType;
	}

	/**
	 * Aktualisiert die tatsächliche Ankunftszeit des Zuges
	 *
	 * @param actualArrival Die neue tatsächliche Ankunftszeit.
	 */
	public void updateArrival(LocalTime actualArrival) {
		this.actualArrival = actualArrival;
	}

	/**
	 * Aktualisiert die tatsächliche Abfahrtszeit des Zuges
	 *
	 * @param actualDeparture Die neue tatsächliche Abfahrtszeit
	 */
	public void updateDeparture(LocalTime actualDeparture) {
		this.actualDeparture = actualDeparture;
	}

	/**
	 * Aktualisiert den tatsächlichen Gleis, an de, der Zug hält.
	 *
	 * @param actualTrack Der neue Gleis.
	 */
	public void updateTrack(String actualTrack) {
		if (actualTrack == null)
			throw new IllegalArgumentException("actualTrack darf nicht null sein");
		this.actualTrack = actualTrack;
	}

	/**
	 * Aktualisiert die aktuelle Meldung
	 *
	 * @param messageId
	 */
	public void updateMessage(int messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return Die Id der Message
	 */
	public int getMessageId() {
		return messageId;
	}

	/**
	 * @return Geplante Abfahrtszeit
	 */
	public LocalTime getScheduledDeparture() {
		return scheduledDeparture;
	}

	/**
	 * @return Geplante Ankunftszeit
	 */
	public LocalTime getScheduledArrival() {
		return scheduledArrival;
	}

	/**
	 * @return Tatsächliche Abfahrtszeit
	 */
	public LocalTime getActualDeparture() {
		return actualDeparture;
	}

	/**
	 * @return Tatsächliche Ankunftszeit
	 */
	public LocalTime getActualArrival() {
		return actualArrival;
	}

	/**
	 * @return Geplanter Gleis
	 */
	public String getScheduledTrack() {
		return scheduledTrack;
	}

	/**
	 * @return Tatsächlicher Gleis
	 */
	public String getActualTrack() {
		return actualTrack;
	}

	/**
	 * @return Der Zuglauf {@link TrainRoute}, der diesen Halt beinhaltet (muss
	 * natürlich vorher gesetzt worden sein!)
	 */
	public TrainRoute getTrainRoute() {
		return trainRoute;
	}
	
	/**
	 * Berechnet die Verspätung bei der Ankunft in Sekunden.
	 *
	 * @return Positives Ergebnis, wenn der Zug zu spät ankommt
	 * Negatives Ergebnis, wenn der Zug zu früh ankommt.
	 */
	public long getDelayArrival() {	
		if(this.scheduledArrival == null || this.actualArrival == null){
			return 0;
		}
		if(this.actualArrivalNextDay){
			// +1 wegen Rundung von 24:00 (bzw. 00:00) auf 23:59
			return (this.scheduledArrival.until(LocalTime.MAX, ChronoUnit.SECONDS) + 1 + LocalTime.MIDNIGHT.until(this.actualArrival, ChronoUnit.SECONDS));
		}	else{
			return this.scheduledArrival.until(this.actualArrival, ChronoUnit.SECONDS);
		}
	}

	/**
	 * Berechnet die Verspätung bei der Abfahrt in Sekunden.
	 *
	 * @return Positives Ergebnis, wenn der Zug zu spät abfährt
	 * Negatives Ergebnis, wenn der Zug zu früh abfährt.
	 */
	public long getDelayDeparture() {
		if(this.scheduledDeparture == null || this.actualDeparture == null){
			return 0;
		}
		if(this.actualDepartureNextDay){
			// +1 wegen Rundung von 24:00 (bzw. 00:00) auf 23:59
			return (this.scheduledDeparture.until(LocalTime.MAX, ChronoUnit.SECONDS) + 1 + LocalTime.MIDNIGHT.until(this.actualDeparture, ChronoUnit.SECONDS));
		}	else{
			return this.scheduledDeparture.until(this.actualDeparture, ChronoUnit.SECONDS);
		}
	}

	/**
	 * @return Liefert die Ankunftsverspätung in der Form [-]MM.SS
	 */
	public String getDelayArrivalString() {
		long delay = this.getDelayArrival();
		String str = "";
		Duration d = Duration.ofSeconds(delay, 0);
		if (!d.equals(Duration.ZERO)){
			str = String.format("%+d", d.toMinutes());
		}
		/*if (delay < 0) {
			LocalTime time = LocalTime.MIDNIGHT.plus(0 - delay, ChronoUnit.SECONDS);
			String min = Integer.toString(time.getMinute());
			if (min.length() == 1) {
				min = "0" + min;
			}
			String sec = Integer.toString(time.getSecond());
			if (sec.length() == 1) {
				sec = "0" + sec;
			}
			str = "-" + min + ":" + sec;
		} else if (delay > 0) {
			LocalTime time = LocalTime.MIDNIGHT.plus(delay, ChronoUnit.SECONDS);
			String min = Integer.toString(time.getMinute());
			if (min.length() == 1) {
				min = "0" + min;
			}
			String sec = Integer.toString(time.getSecond());
			if (sec.length() == 1) {
				sec = "0" + sec;
			}
			str = "+" + min + ":" + sec;
		}*/
		return str;
	}

	
	/**
	 * @return Liefert die Abfahrtsverspätung in der Form [-]MM.SS
	 */
	public String getDelayDepartureString() {
		long delay = this.getDelayDeparture();
		String str = "";
		Duration d = Duration.ofSeconds(delay, 0);
		if (!d.equals(Duration.ZERO)){
			str = String.format("%+d", d.toMinutes());
		}
		
		/*if (delay < 0) {
			LocalTime time = LocalTime.MIDNIGHT.plus(0 - delay, ChronoUnit.SECONDS);
			String min = Integer.toString(time.getMinute());
			if (min.length() == 1) {
				min = "0" + min;
			}
			String sec = Integer.toString(time.getSecond());
			if (sec.length() == 1) {
				sec = "0" + sec;
			}
			str = "-" + min + ":" + sec;
		} else if (delay > 0) {
			LocalTime time = LocalTime.MIDNIGHT.plus(delay, ChronoUnit.SECONDS);
			String min = Integer.toString(time.getMinute());
			if (min.length() == 1) {
				min = "0" + min;
			}
			String sec = Integer.toString(time.getSecond());
			if (sec.length() == 1) {
				sec = "0" + sec;
			}
			str = "+" + min + ":" + sec;
		}*/
		return str;
	}
	

}
