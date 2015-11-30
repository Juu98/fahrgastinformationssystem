package fis.data;

import java.time.LocalTime;
import java.util.ArrayList;
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
 * Controller für {@link TimetableData}
 * Beinhaltet Weiterreichen von einkommenden Telegrammen und Aktionen, die vom ConnectionState abhängen (z.B. Entscheidung, dass RailML-Fahrplan geladen werden soll)
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
	 * @return Aktuelle Laborzeit (falls verfügbar)
	 */
	public LocalTime getTime(){
		return LocalTime.now();
	}
	
	/**
	 * @return "Rohe" Fahrplandatenstruktur
	 */
	public TimetableData getData(){
		return data;
	}
	
	/**
	 * Empfängt ein Telegram und führt Updates der Datenstruktur durch oder aktualisiert die Laborzeit (je nach Telegram)
	 * @param telegram Telegram to 
	 */
	public void receiveTelegram(){
		
	}
		
	/**
	 * @return Stringrepräsentation des aktuellen ConnectionStates
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
	 * @see Station.getStops
	 * @param station
	 * @return Alle Halte eines Bahnhofs, wenn station nicht null ist. Falls station null ist, gibt die Funktion eine leere Liste zurück.
	 */
	public List<Stop> getStopsByStation(Station station){
		if(station==null) return new ArrayList<Stop>();
		return station.getStops();
	}
	
	/**
	 * Gibt alle Zugläufe eines Bahnhofs aus
	 * @param station
	 * @param type Je nach {@link FilterType} gibt die Funktion alle Zugläufe, ein- oder ausfahrende Zugläufe an.
	 * @return Alle Zugläufe eines Bahnhofs, sofern station nicht null ist (sonst leere Liste)
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
			return new ArrayList<>();
		}
		
		List<Stop> newList=new ArrayList<>();
		for(TrainRoute route:listToFilter){
			for(Stop stop:route.getStops()){
				if(stop.getStation()==station){
					LocalTime stopTime;
				
					switch(filterTime){
					case SCHEDULED:
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
						}
					}
				}
			}
			
		}
		return newList;	
	}
	
		
}
	

	
	
	

