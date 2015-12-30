/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fis.web;

import fis.data.Station;
import  fis.data.Stop;
import fis.data.TrainRoute;
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
		private int x;
		private int y;
		
		public StationView(String id, String name, int x, int y){
			this.id = id;
			this.name = name;
			this.x = x;
			this.y = y;
		}
		public StationView(Station s){
			this.id = s.getId();
			this.name = s.getLongName();
			this.x = s.getPosX();
			this.y = s.getPosY();
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}
		
		public int getX(){
			return x;
		}
		
		public int getY(){
			return y;
		}
		
	}
	
	public class TrainRouteView {
		private String id;
		private String name;
		private StationView begin;
		private StationView end;
		public TrainRouteView(String id, StationView begin, String name, StationView end){
			this.id = id;
			this.name = name;
			this.begin = begin;
			this.end = end;
		}
		public TrainRouteView(TrainRoute tr){
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
	
	
	
	//
	
	public class FullTrainRouteView {
		private String id;
		private String name;
		private List<StationView> stops=new ArrayList<StationView>();
		public FullTrainRouteView(String id, List<StationView> stops, String name){
			this.id = id;
			this.name = name;
			this.stops=stops;
		}
		public FullTrainRouteView(TrainRoute tr){
			this.id = tr.getId();
			this.name = tr.toString();
			for(Stop stop:tr.getStops()){
				stops.add(new StationView(stop.getStation()));
			}
		}

		public String getId() {
			return id;
		}
		
		public String getName() {
			return name;
		}

		public List<StationView> getStops() {
			return stops;
		}
		
	}
	
	public List<FullTrainRouteView> getFullTrainRoutes(List<TrainRoute> input){
		List<FullTrainRouteView> ret = new ArrayList<>();
		for (TrainRoute tr : input){
			FullTrainRouteView trv = new FullTrainRouteView(tr);
			ret.add(trv);
		}
		
		return ret;
	}
	
}
