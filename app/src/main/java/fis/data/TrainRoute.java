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

import java.util.ArrayList;
import java.util.List;

/**
 * Eine Klasse für einen Zuglauf
 *
 * @author Luux
 */
public class TrainRoute {
	private String id;
	private String trainNumber;
	private int messageId;
	private TrainCategory trainCategory;
	private List<Stop> stops = new ArrayList<Stop>();

	/**
	 * @param id            Zuglauf-ID
	 * @param trainNumber   Zugnummer
	 * @param trainCategory Zugkategorie. Siehe {@link TrainCategory}.
	 * @param stops         List von allen {@link Stop}s dieses Zuglaufs.
	 */
	public TrainRoute(String id, String trainNumber, TrainCategory trainCategory, List<Stop> stops, int messageId) {
		if (id == null || trainCategory == null || stops == null) {
			throw new IllegalArgumentException("id, trainCategory und stops dürfen nicht null sein!");
		}

		this.id = id;
		this.trainNumber = trainNumber;
		this.trainCategory = trainCategory;
		this.stops = stops;
		this.messageId = messageId;

		// link stops
		for (Stop stop : stops) {
			linkStop(stop);
		}
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
	 * @return ID des Zuglaufs
	 */
	public String getId() {
		return id;
	}

	/**
	 * Verlinkt den gegebenen Stop zu diesem Zuglauf
	 *
	 * @param stop
	 */
	public void linkStop(Stop stop) {
		if (stop != null)
			stop.setTrainRoute(this);
	}

	/**
	 * Entfernt alle Stops
	 */
	public void removeStops() {
		while (stops.size() > 0) {
			stops.get(0).deleteStop();
		}
	}

	/**
	 * Aktualisiert die Zugnummer
	 *
	 * @param newNr
	 */
	public void setTrainNumber(String newNr) {
		this.trainNumber = newNr;
	}

	/**
	 * @return Zugnummer
	 */
	public String getTrainNumber() {
		return trainNumber;
	}

	public void setTrainCategory(TrainCategory newCat) {
		if (newCat == null) {
			throw new IllegalArgumentException("newCat darf nicht null sein!");
		}

		this.trainCategory = newCat;
	}

	/**
	 * @return Zugkategorie. Siehe {@link TrainCategory}.
	 */
	public TrainCategory getTrainCategory() {
		return trainCategory;
	}

	/**
	 * @return Eine Liste von allen {@link Stop}s dieses Zuglaufs
	 */
	public List<Stop> getStops() {
		return stops;
	}

	/**
	 * Hängt die gegebene Liste von Halten an
	 *
	 * @param stops2add Liste von Halten
	 */
	public void addStops(List<Stop> stops2add) {
		for (Stop stop : stops2add) {
			linkStop(stop);
		}

		this.stops.addAll(stops2add);
	}

	/**
	 * @return Das Ende des Zuglaufs
	 */
	public Stop getLastStop() {
		return stops.get(stops.size() - 1);
	}

	/**
	 * @return Den Anfang des Zuglaufs
	 */

	public Stop getFirstStop() {
		return stops.get(0);
	}

	/**
	 * Funktion zum bestimmen des Stops einer Station in einem Zuglauf.
	 *
	 * @param station
	 * @return die gefundene Station; sonst {@literal null}
	 * @throws IllegalArgumentException
	 */
	public Stop getStopAtStation(Station station) throws IllegalArgumentException {
		if (station == null)
			throw new IllegalArgumentException();
		for (Stop stop : this.getStops()) {
			if (stop.getStation().equals(station) && (stop.getStopType().equals(StopType.BEGIN)
					|| stop.getStopType().equals(StopType.END) || stop.getStopType().equals(StopType.STOP)))
				return stop;
		}
		return null;
	}

	/**
	 * Liefert eine menschenlesbare Zugnummer.
	 *
	 * @return Zugnummer in der Form ICE 123
	 */
	@Override
	public String toString() {
		if (this.trainCategory != null)
			return String.format("%s %s", this.trainCategory.getName(), this.trainNumber);
		else
			return this.id;
	}
}
