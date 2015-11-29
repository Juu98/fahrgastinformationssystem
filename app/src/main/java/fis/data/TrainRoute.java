package fis.data;

import fis.FilterType;
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
	
	public boolean containsStation(Station station){
		return containsStation(station, FilterType.ANY);
	}
	public boolean containsStation(Station station, FilterType filterType){
		boolean isNull = false;
		
		for (Stop s : this.stops){
			if (s.getStation() == null){
				System.out.println("Stop-Bahnhof ist NULL!");
				s.printDebugInformation();
				isNull = true;
			}
			
			if (isNull){
				debugPrint();
				return false;
			}
			
			if (s.getStation().equals(station)){
				switch (filterType){
					// show only arriving trains, not those beginning at this station
					case ARRIVAL: return (s.getStopType() != StopType.BEGIN);
					// show only departing trains, not those ending at this station
					case DEPARTURE: return (s.getStopType() != StopType.END);
					// show it anyway
					default: return true;
				}
			}
		}
		return false;
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
	
	@Override
	public String toString(){
		return String.format("%s %s", this.trainCategory.getName(), this.trainNumber);
	}
}
