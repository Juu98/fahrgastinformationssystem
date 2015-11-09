package fis;

import java.util.ArrayList;
import java.util.List;

public class TrainRoute {
	private String id;
	private int trainNumber;
	private TrainType type;
	private List<Stop> stops=new ArrayList<Stop>();
	
	public TrainRoute(String id,int trainNumber, TrainType type, List<Stop> stops){
		this.id=id;
		this.trainNumber=trainNumber;
		this.type=type;
		this.stops=stops;
	}
	
	public String getId(){
		return id;
	}
	public int getTrainNumber(){
		return trainNumber;
	}
	
	public TrainType getType(){
		return type;
	}
	public List<Stop> getStops(){
		return stops;
	}
	
	
	
	
}
