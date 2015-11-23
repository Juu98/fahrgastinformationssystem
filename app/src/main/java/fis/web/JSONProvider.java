/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fis.web;

import fis.Station;
import fis.TrainRoute;
import java.util.ArrayList;
import java.util.List;

/**
 * Static methods to convert the Lists into nice JSON.
 * @author Robert
 */
public class JSONProvider {
	public class StationView {
		private String id;
		private String name;
		public StationView(String id, String name){
			this.id = id;
			this.name = name;
		}
		public StationView(Station s){
			this.id = s.getId();
			this.name = s.getName();
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}
		
	}
	
	public class TrainRouteView {
		private String id;
		private StationView begin;
		private StationView end;
		public TrainRouteView(String id, StationView begin, StationView end){
			this.id = id;
			this.begin = begin;
			this.end = end;
		}
		public TrainRouteView(TrainRoute tr){
			this.id = tr.getId();
			this.begin = new StationView(tr.getStops().get(0).getStation());
			this.end = new StationView(tr.getStops().get(tr.getStops().size()-1).getStation());
		}

		public String getId() {
			return id;
		}

		public StationView getBegin() {
			return begin;
		}

		public StationView getEnd() {
			return end;
		}
		
	}
	
	public List<StationView> getStations(List<Station> input){
		List<StationView> ret = new ArrayList<>();
		for (Station s : input){
			StationView sv = new StationView(s);
			ret.add(sv);
		}
		
		return ret;
	}
	
	public List<TrainRouteView> getTrainRoutes(List<TrainRoute> input){
		List<TrainRouteView> ret = new ArrayList<>();
		for (TrainRoute tr : input){
			TrainRouteView trv = new TrainRouteView(tr);
			ret.add(trv);
		}
		
		return ret;
	}
}
