/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fis.web;

import fis.data.Station;
import fis.data.TrainRoute;
import java.util.ArrayList;
import java.util.List;

/**
 * Static methods to convert the Lists into nice JSON.
 * 
 * @author Robert
 */
public class JSONProvider {
	/**
	 * Eine Klasse für die JSON optimierten Bahnhofsdaten. 
	 * @author Robert, kloppstock
	 */
	public static class StationView {
		private String id;
		private String name;

		/**
		 * Konstruktor für die Bahnhofsdatenstruktur.
		 * @param id
		 * @param name
		 * @throws IllegalArgumentException
		 */
		public StationView(String id, String name) throws IllegalArgumentException {
			if (id == null || name == null)
				throw new IllegalArgumentException("One of the given parameters is null!");
			
			this.id = id;
			this.name = name;
		}

		/**
		 * Konstruktor für die Bahnhofsdatenstruktur.
		 * @param s (Station)
		 * @throws IllegalArgumentException
		 */
		public StationView(Station s) throws IllegalArgumentException {
			if (s == null)
				throw new IllegalArgumentException("The given parameter is null!");
			
			this.id = s.getId();
			this.name = s.getLongName();
		}

		/**
		 * Getter für id
		 * @return id
		 */
		public String getId() {
			return id;
		}

		/**
		 * Getter für name
		 * @return name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Prüft ob der StationView einem anderen Obkjekt gleicht.
		 * 
		 * @param other
		 * @return {@literal true}, wenn die Objekte gleich sind, sonst
		 *         {@literal false}
		 */
		@Override
		public boolean equals(Object other) {
			if (other == null)
				return false;
			if (other.getClass() != this.getClass())
				return false;
			if (((StationView) other).getId().equals(this.getId())
					&& ((StationView) other).getName().equals(this.getName()))
				return true;
			return false;
		}

	}

	/**
	 * Eine Klasse für die JSON optimierten Zuglaufsdaten. 
	 * @author Robert, kloppstock
	 *
	 */
	public static class TrainRouteView {
		private String id;
		private String name;
		private StationView begin;
		private StationView end;

		/**
		 * Konstruktor für die Zuglaufdatenstruktur.
		 * @param id
		 * @param begin
		 * @param name
		 * @param end
		 * @throws IllegalArgumentException
		 */
		public TrainRouteView(String id, StationView begin, String name, StationView end)
				throws IllegalArgumentException {
			if (id == null || begin == null || name == null || end == null)
				throw new IllegalArgumentException("One of the given parameters is null!");
			
			this.id = id;
			this.name = name;
			this.begin = begin;
			this.end = end;
		}

		/**
		 * Konstruktor für die Bahnhofsdatenstruktur.
		 * @param tr (Zuglauf)
		 * @throws IllegalArgumentException
		 */
		public TrainRouteView(TrainRoute tr) throws IllegalArgumentException {
			if (tr == null)
				throw new IllegalArgumentException("The given parameter is null!");
			
			this.id = tr.getId();
			this.name = tr.toString();
			this.begin = new StationView(tr.getStops().get(0).getStation());
			this.end = new StationView(tr.getStops().get(tr.getStops().size() - 1).getStation());
		}

		/**
		 * Getter für id
		 * @return id
		 */
		public String getId() {
			return id;
		}

		/**
		 * Getter für name
		 * @return name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Getter für begin
		 * @return begin
		 */
		public StationView getBegin() {
			return begin;
		}

		/**
		 * Getter für end
		 * @return end
		 */
		public StationView getEnd() {
			return end;
		}

		/**
		 * Prüft ob der TrainRouteView einem anderen Obkjekt gleicht.
		 * 
		 * @param other
		 * @return {@literal true}, wenn die Objekte gleich sind, sonst
		 *         {@literal false}
		 */
		@Override
		public boolean equals(Object other) {
			if (other == null)
				return false;
			if (other.getClass() != this.getClass())
				return false;
			if (((TrainRouteView) other).getBegin().equals(this.begin)
					&& ((TrainRouteView) other).getEnd().equals(this.end)
					&& ((TrainRouteView) other).getId().equals(this.id)
					&& ((TrainRouteView) other).getName().equals(this.name))
				return true;
			return false;
		}

	}

	/**
	 * Methode zum Erstellen einer Liste von optimierten Bahnhofsdaten aus nicht optimierten Bahnhofsdaten.
	 * @param input (Liste der nicht opimierten Bahnhofsdaten)
	 * @return Liste der opimierten Bahnhofsdaten
	 * @throws IllegalArgumentException
	 */
	public List<StationView> getStations(List<Station> input) throws IllegalArgumentException {
		if (input == null)
			throw new IllegalArgumentException("The given parameter is null!");
		
		List<StationView> ret = new ArrayList<>();
		for (Station s : input) {
			StationView sv = new StationView(s);
			ret.add(sv);
		}

		return ret;
	}

	/**
	 * Methode zum Erstellen einer Liste von optimierten Zuglaufdaten aus nicht optimierten Zuglaufdaten.
	 * @param input (Liste der nicht opimierten Zuglaufdaten)
	 * @return Liste der opimierten Zuglaufdaten
	 * @throws IllegalArgumentException
	 */
	public List<TrainRouteView> getTrainRoutes(List<TrainRoute> input) throws IllegalArgumentException {
		if (input == null)
			throw new IllegalArgumentException("The given parameter is null!"); 
		
		List<TrainRouteView> ret = new ArrayList<>();
		for (TrainRoute tr : input) {
			TrainRouteView trv = new TrainRouteView(tr);
			ret.add(trv);
		}

		return ret;
	}
}
