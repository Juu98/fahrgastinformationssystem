package fis;

import java.util.ArrayList;
import java.util.List;

//"Rohe" Fahrplandaten
public class TimetableData {
	private List<TrainRoute> trainroutes;
	private List<Station> stations;
	
	public TimetableData(){
		trainroutes=new ArrayList<TrainRoute>();
		stations=new ArrayList<Station>();
	}
	
	public void addZuglauf(TrainRoute trainRoute){
		trainroutes.add(trainRoute);
	}
	
	public List<TrainRoute> gettrainroutes(){
		return trainroutes;
	}
	
	public List<Station> getStations(){
		return stations;
	}
	
	public void addStation(Station station){
		stations.add(station);
	}
	
	public Station getStationByID(String id){
		for(Station station:stations){
			if(station.getId().equals(id)){
				System.out.println("Bahnhof mit der ID "+id+": "+station.getName());
				return station;
			}
		}
		System.out.println("ACHTUNG: Bahnhof mit der ID "+id + " nicht gefunden!");
		return null;
	}

	
	
}
