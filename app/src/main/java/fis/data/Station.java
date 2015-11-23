package fis.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Station {
	private String name;
	private String id;
	private List<Stop> stops;
	
	public Station(String id, String name){
		this.name=name;
		this.id=id;
		this.stops=new LinkedList<Stop>();
	}
	
	public String getName(){
		return name;
	}
	
	public String getId(){
		return id;
	}
	
	public void addStop(Stop stop){
		stops.add(stop);
	}
	
	public List<Stop> getStops(){
		return stops;
	}
	
	public boolean hasStop(Stop stop){
		return stops.contains(stop);
	}
	
	public void removeStop(Stop stop){
		stop.deleteStop();
	}
	
	
}
