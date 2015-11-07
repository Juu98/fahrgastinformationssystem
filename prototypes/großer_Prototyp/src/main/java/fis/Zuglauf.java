package fis;

import java.util.ArrayList;
import java.util.List;

public class Zuglauf {
	private String id;
	private int trainNumber;
	private TrainType type;
	private List<Halt> stops=new ArrayList<Halt>();
	
	public Zuglauf(String id,int trainNumber, TrainType type, List<Halt> stops){
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
	public List<Halt> getStops(){
		return stops;
	}
	
	
	
	
}
