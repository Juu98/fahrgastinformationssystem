package fis;

import java.util.ArrayList;
import java.util.List;

//"Rohe" Fahrplandaten
public class TimetableData {
	private List<TrainRoute> trainroutes;
	private List<Station> stations;
	private List<TrainCategory> trainCategories;
	
	public TimetableData(){
		trainroutes=new ArrayList<TrainRoute>();
		stations=new ArrayList<Station>();
		trainCategories=new ArrayList<TrainCategory>();
	}
	
	public void addTrainRoute(TrainRoute trainRoute){
		trainroutes.add(trainRoute);
	}
	
	public List<TrainRoute> getTrainRoutes(){
		return trainroutes;
	}
	
	public List<Station> getStations(){
		return stations;
	}
	
	public List<TrainCategory> getTrainCategories(){
		return trainCategories;
	}
	
	public void addTrainCategory(TrainCategory category){
		trainCategories.add(category);
	}
	
	public void addStation(Station station){
		stations.add(station);
	}
	
	public Station getStationByID(String id){
		if(id==null) throw new NullPointerException();
		for(Station station:stations){
			if(station.getId().equals(id)){
				System.out.println("Bahnhof mit der ID "+id+": "+station.getName());
				return station;
			}
		}
		System.out.println(id + " seems to be NO station!");
		return null;
	}
	
	public TrainCategory getTrainCategoryById(String id){
		for(TrainCategory cat:trainCategories){
			if(cat.getId().equals(id)){
				return cat;
			}
		}
		System.out.println("ACHTUNG: TrainCategory mit der ID "+id+" nicht gefunden!");
		return null;
	}

	public TrainRoute getTrainRouteById(String id){
		if (id == null){
			throw new NullPointerException("TrainRoute.ID must not be null.");
		}
		
		for (TrainRoute tr : trainroutes){
			if (tr.getId().equals(id)){
				return tr;
			}
		}
		
		System.out.println("!!! TrainRoute [" + id + "] not found!");
		return null;
	}
	
}
