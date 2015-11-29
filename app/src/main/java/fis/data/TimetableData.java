package fis.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Zeitplandatenstruktur
 * Enthält Rohdaten. Keine Beachtung von Konnektivitätsstatus, Filtern (außer getByID-Funktionen) o.Ä.
 * @author Eric
 *
 */
public class TimetableData {
	private List<TrainRoute> trainRoutes;
	private List<Station> stations;
	private List<TrainCategory> trainCategories;
	
	/**
	 * Initialisiert TimetableData
	 */
	public TimetableData(){
		trainRoutes=new ArrayList<TrainRoute>();
		stations=new ArrayList<Station>();
		trainCategories=new ArrayList<TrainCategory>();
		
	}
	
	/**
	 * Fügt den gegebenen Zuglauf zur Datenstruktur hinzu
	 * @param trainRoute {@link TrainRoute} zum Hinzufügen
	 */
	public void addTrainRoute(TrainRoute trainRoute){
		trainRoutes.add(trainRoute);
	}
	
	/** 
	 * Getter für trainRoutes
	 * @return Eine Liste aller verfügbaren {@link TrainRoute}s in der Datenstruktur
	 */
	public List<TrainRoute> getTrainRoutes(){
		return trainRoutes;
	}
	
	/**
	 * Getter für stations
	 * @return Liste von allen verfügbaren {@link Station}s
	 */
	public List<Station> getStations(){
		return stations;
	}
	
	/**
	 * Getter für trainCategories
	 * @return Liste von allen verfügbaren {@link TrainCategory}
	 */
	public List<TrainCategory> getTrainCategories(){
		return trainCategories;
	}
	
	/**
	 * Fügt die gegebene {@link TrainCategory} hinzu
	 * @param category
	 */
	public void addTrainCategory(TrainCategory category){
		trainCategories.add(category);
	}
	
	/**
	 * Fügt den gegebenen Bahnhof {@link Station} hinzu.
	 * @param station
	 */
	public void addStation(Station station){
		stations.add(station);
	}
	
	/**
	 * @param id Die gesuchte ID
	 * @return {@link Station} mit der gesuchten ID (falls verfügbar)
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
	 * @param id Die gesuchte ID
	 * @return {@link TrainCategory} mit der gesuchten ID (falls verfügbar)
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
	 * @param id Die gesuchte ID
	 * @return {@link TrainRoute} mit der gesuchten ID (falls verfügbar)
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
