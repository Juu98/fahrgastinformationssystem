package fis;

import java.util.ArrayList;
import java.util.List;

//"Rohe" Fahrplandaten
public class FahrplanData {
	private List<Zuglauf> zuglaeufe;
	private List<Bahnhof> stations;
	
	public FahrplanData(){
		zuglaeufe=new ArrayList<Zuglauf>();
		stations=new ArrayList<Bahnhof>();
	}
	
	public void addZuglauf(Zuglauf zuglauf){
		zuglaeufe.add(zuglauf);
	}
	
	public List<Zuglauf> getZuglaeufe(){
		return zuglaeufe;
	}
	
	public List<Bahnhof> getStations(){
		return stations;
	}
	
	public void addStation(Bahnhof station){
		stations.add(station);
	}
	
	public Bahnhof getStationByID(String id){
		for(Bahnhof station:stations){
			if(station.getId().equals(id)) return station;
		}
		
		return null;
	}
	
	
}
