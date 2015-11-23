package fis.data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fis.FilterTime;
import fis.FilterType;
import fis.railml2data;
import fis.telegramReceiver.TelegramReceiver;

/**
 * Controller for TimetableData. 
 * Contains forwarding of incoming Telegrams and handling the connection state (e.g. deciding when to load RailML)
 * @author Eric
 */
@Component
public class TimetableController {
	private TimetableData data;
	@Autowired private TelegramReceiver receiver;
	
	public TimetableController(){
		try{
			data=railml2data.loadML("2015-04-27_EBL-Regefahrplan-Export.xml");	
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
	
	
	public List<TrainRoute> filterByStation(List<TrainRoute> listToFilter, Station stationIncluded){
		/* 
		 * filtert die gegebene Liste mit Zugläufen nach dem angegebenen Bahnhof
		 * Ausgabeliste enthält alle Zugläufe, die den angegebenen Bahnhof enthalten 
		*/
		
		List<TrainRoute> newList=new ArrayList<>();
		
		if(stationIncluded==null) {
			System.out.println("Filter got NULL-Station!");
			return new ArrayList<TrainRoute>();
		}
		
		for (TrainRoute route : listToFilter){
			if(route.containsStation(stationIncluded)){
				newList.add(route);
			}
		}
		return newList;		
	}
			
	public List<Stop> filter(List<TrainRoute> listToFilter, Station station, LocalTime from, LocalTime to, FilterType type,FilterTime filterTime){
		/* 
		 * Filtert die gegebene Liste nach der Ankunfts-/Abfahrtszeit in der Zeit von [from] bis [to] am angegebenen Bahnhof
		 * und gibt alle entsprechenden "Stop"-Objekte, die mit diesem Bahnhof assoziiert sind, zurück
		 * TODO: Testen (ist garantiert voller Fehler...)
		 */
		if(station==null) {
			System.out.println("Filter got NULL-Station!");
			return new ArrayList<Stop>();
		}
		
		List<Stop> newList=new ArrayList<Stop>();
		for(TrainRoute route:listToFilter){
			for(Stop stop:route.getStops()){
				if(stop.getStation()==station){
					LocalTime stopTime;
				
					switch(filterTime){
					case Scheduled:
						//Es soll nach Scheduled gefiltert werden
						if(type==FilterType.Arrival){
							stopTime=stop.getScheduledArrival();
						} else {
							stopTime=stop.getScheduledDeparture();
						}
						break;
				
					default:
						//Es soll nach Actual gefiltert werden
						if(type==FilterType.Arrival){
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
	

	
	
	

