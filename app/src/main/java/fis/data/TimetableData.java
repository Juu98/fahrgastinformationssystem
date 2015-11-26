package fis.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Timetable datastructure.
 * Contains raw data. Free of any state or filtering logic, just storing and updating data. (Except the getByID-Functions)
 * @author Eric
 *
 */
public class TimetableData {
	private List<TrainRoute> trainroutes;
	private List<Station> stations;
	private List<TrainCategory> trainCategories;
	
	/**
	 * Initialize TimetableData
	 */
	public TimetableData(){
		trainroutes=new ArrayList<TrainRoute>();
		stations=new ArrayList<Station>();
		trainCategories=new ArrayList<TrainCategory>();
		
	}
	
	/**
	 * Adds the given TrainRoute to the datastructure
	 * @param trainRoute TrainRoute to add
	 */
	public void addTrainRoute(TrainRoute trainRoute){
		trainroutes.add(trainRoute);
	}
	
	/** 
	 * Getter for TrainRoutes
	 * @return A list of all available {@link TrainRoute}s currently stored in TimetableData
	 */
	public List<TrainRoute> getTrainRoutes(){
		return trainroutes;
	}
	
	/**
	 * Getter for Stations
	 * @return A list of all available {@link Station}s
	 */
	public List<Station> getStations(){
		return stations;
	}
	
	/**
	 * Getter for TrainCategories
	 * @return A list of all available instancs of {@link TrainCategory}
	 */
	public List<TrainCategory> getTrainCategories(){
		return trainCategories;
	}
	
	/**
	 * Adds the given {@link TrainCategory} to the data structure
	 * @param category The TrainCategory to add
	 */
	public void addTrainCategory(TrainCategory category){
		trainCategories.add(category);
	}
	
	/**
	 * Adds the given {@link Station} to the data structure
	 * @param station The station to add
	 */
	public void addStation(Station station){
		stations.add(station);
	}
	
	/**
	 * @param id The id to search
	 * @return {@link Station} with the given ID (if available)
	 */
	public Station getStationByID(String id){
		if(id==null) throw new NullPointerException();
		for(Station station:getStations()){
			if(station.getId().equals(id)){
				System.out.println("Bahnhof mit der ID "+id+": "+station.getName());
				return station;
			}
		}
		System.out.println(id + " seems to be NO station!");
		return null;
	}
	
	/**
	 * @param id The ID to search
	 * @return {@link TrainCategory} with the given ID (if available)
	 */
	public TrainCategory getTrainCategoryById(String id){
		for(TrainCategory cat:getTrainCategories()){
			if(cat.getId().equals(id)){
				return cat;
			}
		}
		System.out.println("ACHTUNG: TrainCategory mit der ID "+id+" nicht gefunden!");
		return null;
	}


	/**
	 * @param id The ID to search
	 * @return {@link TrainRoute} with the given ID (if available)
	 */
	public TrainRoute getTrainRouteById(String id){
		if (id == null){
			throw new NullPointerException("TrainRoute.ID must not be null.");
		}
		
		for (TrainRoute tr : getTrainRoutes()){
			if (tr.getId().equals(id)){
				return tr;
			}
		}
		
		System.out.println("!!! TrainRoute [" + id + "] not found!");
		return null;
	}

	
}
