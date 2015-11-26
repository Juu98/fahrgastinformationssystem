package fis.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A station class. Contains all the information of a station for the data structure.
 * @author Luux
 *
 */
public class Station {
	private String name;
	private String id;
	private List<Stop> stops;
	
	/**
	 * 
	 * @param id The ID of the station
	 * @param name The name of the station
	 */
	public Station(String id, String name){
		this.name=name;
		this.id=id;
		this.stops=new LinkedList<Stop>();
	}
	
	/**
	 * 
	 * @return The name of this station.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 * @return The ID of this station.
	 */
	public String getId(){
		return id;
	}
	
	public void addStop(Stop stop){
		stops.add(stop);
	}
	
	/** 
	 * @return A list of all stops at this station. To access from gui, better use {TimetableController.getStopsByStation}, since it checks if the station is null.
	 */
	public List<Stop> getStops(){
		return stops;
	}
	
	/**
	 * Checks if the given stop is at this station
	 * @param stop The stop to check
	 * @return If the stop is at this station: True; If not: False 
	 */
	public boolean hasStop(Stop stop){
		return stops.contains(stop);
	}
	
	/**
	 * Removes the given stop (if possible). Calls {@link Stop.deleteStop} for data consistency.
	 * @param stop The stop to remove.
	 */
	public void removeStop(Stop stop){
		stop.deleteStop();
	}
	
	@Override
	public String toString(){
		return String.format("[%s] %s", id, name);
	}
}
