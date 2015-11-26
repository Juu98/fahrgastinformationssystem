package fis.data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fis.FilterTime;
import fis.FilterType;
import fis.RailML2Data;
import fis.telegramReceiver.TelegramReceiver;

/**
 * Controller for TimetableData. 
 * Contains forwarding of incoming Telegrams and handling the connection state (e.g. deciding when to load RailML)
 * @author Luux
 */
@Component
public class TimetableController {
	private TimetableData data;
	@Autowired private TelegramReceiver receiver;
	
	public TimetableController(){
		try{
			data=RailML2Data.loadML("2015-04-27_EBL-Regefahrplan-Export.xml");	
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
	}
	
	/**
	 * @return The current laboratory time (if available)
	 */
	public LocalTime getTime(){
		return LocalTime.now();
	}
	
	/**
	 * @return Raw timetable data
	 */
	public TimetableData getData(){
		return data;
	}
	
	/**
	 * Receives a telegram and either forwards the updated/new data to the data structure or updates the time depending on telegram type
	 * @param telegram Telegram to 
	 */
	public void receiveTelegram(){
		
	}
		
	/**
	 * @return String representation of the current state (Offline, Online, Connecting)
	 */
	public String getStateName(){
		switch(receiver.getConnectionStatus()){
			case OFFLINE:return "Offline";
			case ONLINE:return "Online";
			case CONNECTING:return "Connecting";
		}
		return "unknown";
	}
	
	
	/**
	 * @see data.getTrainRoutes
	 */
	public List<TrainRoute> getTrainRoutes(){
		return data.getTrainRoutes();
	}
	
	/**
	 * @see data.getStations
	 */
	public List<Station> getStations(){
		return data.getStations();
	}
	
	/**
	 * @see data.getTrainCategories
	 */
	public List<TrainCategory> getTrainCategories(){
		return data.getTrainCategories();
	}
	

	/**
	 * Returns all stops at a station if the station is not null. If it is null, returns an empty List instead.
	 * @see Station.getStops
	 * @param station
	 * @return
	 */
	public List<Stop> getStopsByStation(Station station){
		if(station==null) return new ArrayList<Stop>();
		return station.getStops();
	}
	
	/**
	 * Returns all TrainRoutes at a station if the station is not null. If it is null, returns an empty List instead.
	 * @param station
	 * @return
	 */
	public Set<TrainRoute> getTrainRoutesByStation(Station station, FilterType type){
		if(station==null) return new HashSet<TrainRoute>();
		
		HashSet<TrainRoute> set=new HashSet<TrainRoute>();
		for(Stop stop:station.getStops()){
			if((type==FilterType.ARRIVAL && stop.getStopType()!=StopType.BEGIN) 
					|| (type==FilterType.DEPARTURE && stop.getStopType()!=StopType.END) 
					|| (type==FilterType.ANY)){
				
						set.add(stop.getTrainRoute());
			}
		}
		return set;
	}
	
	
			
	public List<Stop> filter(List<TrainRoute> listToFilter, Station station, LocalTime from, LocalTime to, FilterType type,FilterTime filterTime){
		/* 
		 * Filtert die gegebene Liste nach der Ankunfts-/Abfahrtszeit in der Zeit von [from] bis [to] am angegebenen Bahnhof
		 * und gibt alle entsprechenden "Stop"-Objekte, die mit diesem Bahnhof assoziiert sind, zurück
		 * TODO: Testen (ist garantiert voller Fehler...)
		 */
		if(station==null) {
			System.out.println("Filter got NULL-Station!");
			return new ArrayList<>();
		}
		
		List<Stop> newList=new ArrayList<>();
		for(TrainRoute route:listToFilter){
			for(Stop stop:route.getStops()){
				if(stop.getStation()==station){
					LocalTime stopTime;
				
					switch(filterTime){
					case SCHEDLED:
						//Es soll nach Scheduled gefiltert werden
						if(type==FilterType.ARRIVAL){
							stopTime=stop.getScheduledArrival();
						} else {
							stopTime=stop.getScheduledDeparture();
						}
						break;
				
					default:
						//Es soll nach Actual gefiltert werden
						if(type==FilterType.ARRIVAL){
							stopTime=stop.getActualArrival();
						} else {
							stopTime=stop.getActualDeparture();
						}
						break;
					}
				
					//hier passiert der eigentliche Vergleich
					//Die equals sind laut Test ebenfalls notwendig!
					if(stopTime!=null && from!=null && to!=null){
						if((stopTime.isAfter(from) || stopTime.equals(from)) && (stopTime.isBefore(to) || stopTime.equals(to))){
							newList.add(stop);
							System.out.println("FILTER: Stop at station "+stop.getStation()+" added!");
						}
					}
				}
			}
			
		}
		return newList;	
	}
	
		
}
	

	
	
	

