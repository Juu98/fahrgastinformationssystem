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
package fis.data;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Zeitplandatenstruktur
 * Enthält Rohdaten. Keine Beachtung von Konnektivitätsstatus, Filtern (außer getByID-Funktionen) o.Ä.
 *
 * @author Eric
 */
public class TimetableData {
	private List<TrainRoute> trainRoutes;
	private List<Station> stations;
	private List<TrainCategory> trainCategories;
	private static final Logger LOGGER = Logger.getLogger(TimetableData.class);

	/**
	 * Initialisiert TimetableData
	 */
	public TimetableData() {
		trainRoutes = new ArrayList<TrainRoute>();
		stations = new ArrayList<Station>();
		trainCategories = new ArrayList<TrainCategory>();

	}

	/**
	 * Fügt den gegebenen Zuglauf zur Datenstruktur hinzu
	 *
	 * @param trainRoute {@link TrainRoute} zum Hinzufügen
	 */
	public void addTrainRoute(TrainRoute trainRoute) {
		if (trainRoute == null) throw new IllegalArgumentException("TrainRoute darf nicht null sein!");
		trainRoutes.add(trainRoute);
	}

	/**
	 * Getter für trainRoutes
	 *
	 * @return Eine Liste aller verfügbaren {@link TrainRoute}s in der Datenstruktur
	 */
	public List<TrainRoute> getTrainRoutes() {
		return trainRoutes;
	}

	/**
	 * Getter für stations
	 *
	 * @return Liste von allen verfügbaren {@link Station}s
	 */
	public List<Station> getStations() {
		return stations;
	}

	/**
	 * Getter für trainCategories
	 *
	 * @return Liste von allen verfügbaren {@link TrainCategory}
	 */
	public List<TrainCategory> getTrainCategories() {
		return trainCategories;
	}

	/**
	 * Fügt die gegebene {@link TrainCategory} hinzu
	 *
	 * @param category
	 */
	public void addTrainCategory(TrainCategory category) {
		if (category == null) throw new IllegalArgumentException("Category darf nicht null sein!");
		trainCategories.add(category);
	}

	/**
	 * Fügt den gegebenen Bahnhof {@link Station} hinzu.
	 *
	 * @param station
	 */
	public void addStation(Station station) {
		if (station == null) throw new IllegalArgumentException("Station darf nicht null sein!");
		stations.add(station);
	}

	/**
	 * @param id Die gesuchte ID
	 * @return {@link Station} mit der gesuchten ID (falls verfügbar)
	 */
	public Station getStationById(String id) {
		if (id == null) return null;
		for (Station station : getStations()) {
			if (station.getId().equals(id)) {
				//LOGGER.debug("Bahnhof mit der ID "+id+": "+station.getLongName());
				return station;
			}
		}
		//LOGGER.debug(id + " scheint kein Bahnhof zu sein!");
		return null;
	}

	/**
	 * @param id Die gesuchte ID
	 * @return {@link TrainCategory} mit der gesuchten ID (falls verfügbar)
	 */
	public TrainCategory getTrainCategoryById(String id) {
		if (id == null) return null;
		for (TrainCategory cat : getTrainCategories()) {
			if (cat.getId().equals(id)) {
				return cat;
			}
		}
		LOGGER.debug("TrainCategory mit der ID " + id + " nicht gefunden!");
		return null;
	}


	/**
	 * @param id Die gesuchte ID
	 * @return {@link TrainRoute} mit der gesuchten ID (falls verfügbar)
	 */
	public TrainRoute getTrainRouteById(String id) {
		if (id == null) {
			return null;
		}

		for (TrainRoute tr : getTrainRoutes()) {
			if (tr.getId().equals(id)) {
				return tr;
			}
		}

		LOGGER.debug("TrainRoute [" + id + "] nicht gefunden!");
		return null;
	}


}
