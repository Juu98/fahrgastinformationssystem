package fis;

import java.util.ArrayList;
import java.util.List;

public class TrainRoute {
	private String id;
	private int trainNumber;
	private TrainCategory trainCategory;
	private List<Stop> stops=new ArrayList<Stop>();
	
	public TrainRoute(String id,int trainNumber, TrainCategory trainCategory, List<Stop> stops){
		this.id=id;
		this.trainNumber=trainNumber;
		this.trainCategory=trainCategory;
		this.stops=stops;
	}
	
	public String getId(){
		return id;
	}
	public int getTrainNumber(){
		return trainNumber;
	}
	
	public TrainCategory getTrainCategory(){
		return trainCategory;
	}
	public List<Stop> getStops(){
		return stops;
	}
	
	
	
	
}
