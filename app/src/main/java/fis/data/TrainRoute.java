package fis.data;

import fis.FilterType;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for a single train route.
 * @author Luux
 *
 */
public class TrainRoute {
	private String id;
	private int trainNumber;
	private TrainCategory trainCategory;
	private List<Stop> stops=new ArrayList<Stop>();
	
	/**
	 * @param id The ID of the route
	 * @param trainNumber Number of the train
	 * @param trainCategory Category of the train. See {@link TrainCategory}.
	 * @param stops A list of all {@link Stop}s in the route.
	 */
	public TrainRoute(String id,int trainNumber, TrainCategory trainCategory, List<Stop> stops){
		this.id=id;
		this.trainNumber=trainNumber;
		this.trainCategory=trainCategory;
		this.stops=stops;
		
		//link stops
		for(Stop stop:stops){
			linkStop(stop);
		}
	}
	
	/**
	 * @return ID of the train route.
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Links the given stop to this train route
	 * @param stop
	 */
	public void linkStop(Stop stop){
		if(stop!=null) stop.setTrainRoute(this);
	}
	
	/**
	 * @return Number of the train.
	 */
	public int getTrainNumber(){
		return trainNumber;
	}
	
	/**
	 * @return Category of the train. See {@link TrainCategory}.
	 */
	public TrainCategory getTrainCategory(){
		return trainCategory;
	}
	
	/**
	 * @return Returns a list of all stops in this route.
	 */
	public List<Stop> getStops(){
		return stops;
	}
	
	public void removeStop(Stop stop){
		stop.deleteStop();
	}
	
	
	public void debugPrint(){
		System.out.println("DEBUG PRINT FOR TRAINROUTE WITH ID #"+id);
		for(Stop s:this.stops){
			s.printDebugInformation();
		}
	}
	
	
}
