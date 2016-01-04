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
 * @author Robert
 */
public class JSONProvider {
	public static class StationView {
		private String id;
		private String name;
		public StationView(String id, String name) throws IllegalArgumentException{
			if(id == null || name == null)
				throw new IllegalArgumentException("One of the given parameters is null!"); // TODO: is this needed?
			this.id = id;
			this.name = name;
		}
		
		public StationView(Station s) throws IllegalArgumentException{
			if(s == null)
				throw new IllegalArgumentException("The given parameter is null!"); // TODO: is this needed?
			this.id = s.getId();
			this.name = s.getLongName();
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}
		
		/**
		 * Prüft ob der StationView einem anderen Obkjekt gleicht. 
		 * @param other
		 * @return {@literal true}, wenn die Objekte gleich sind, sonst {@literal false}
		 */
		@Override
		public boolean equals(Object other){
			if(other == null)
				return false;
			if(other.getClass() != this.getClass())
				return false;
			if(((StationView)other).getId().equals(this.getId()) && ((StationView)other).getName().equals(this.getName()))
				return true;
			return false;
		}
		
	}
	
	public static class TrainRouteView {
		private String id;
		private String name;
		private StationView begin;
		private StationView end;
		public TrainRouteView(String id, StationView begin, String name, StationView end) throws IllegalArgumentException{
			if(id == null || begin == null || name == null || end == null)
				throw new IllegalArgumentException("One of the given parameters is null!"); //TODO: is this needed
			this.id = id;
			this.name = name;
			this.begin = begin;
			this.end = end;
		}
		public TrainRouteView(TrainRoute tr) throws IllegalArgumentException{
			if(tr == null)
				throw new IllegalArgumentException("The given parameter is null!"); //TODO: is this needed?
			this.id = tr.getId();
			this.name = tr.toString();
			this.begin = new StationView(tr.getStops().get(0).getStation());
			this.end = new StationView(tr.getStops().get(tr.getStops().size()-1).getStation());
		}

		public String getId() {
			return id;
		}
		
		public String getName() {
			return name;
		}

		public StationView getBegin() {
			return begin;
		}

		public StationView getEnd() {
			return end;
		}
		
	}
	
	public List<StationView> getStations(List<Station> input) throws IllegalArgumentException{
		if(input == null)
			throw new IllegalArgumentException("The given parameter is null!");	//TODO: is this needed?
		List<StationView> ret = new ArrayList<>();
		for (Station s : input){
			StationView sv = new StationView(s);
			ret.add(sv);
		}
		
		return ret;
	}
	
	public List<TrainRouteView> getTrainRoutes(List<TrainRoute> input) throws IllegalArgumentException{
		if(input == null)
			throw new IllegalArgumentException("The given parameter is null!");	//TODO: is this needed?
		List<TrainRouteView> ret = new ArrayList<>();
		for (TrainRoute tr : input){
			TrainRouteView trv = new TrainRouteView(tr);
			ret.add(trv);
		}
		
		return ret;
	}
}
