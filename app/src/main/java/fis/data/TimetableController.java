package fis.data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fis.telegrams.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import fis.FilterTime;
import fis.FilterType;
import fis.RailML2Data;
import fis.telegramReceiver.TelegramReceiverController;
import java.util.function.Predicate;

/**
 * Controller für {@link TimetableData}.
 * Beinhaltet Weiterreichen von einkommenden Telegrammen und Aktionen, die vom ConnectionState abhängen 
 * (z.B. Entscheidung, dass RailML-Fahrplan geladen werden soll)
 * @author Luux
 */
@Component
public class TimetableController {
	/**
	 * Predicate zum Filtern der {@link TrainRoute}s nach ihrer
	 * {@link TrainCategory}.
	 * 
	 * <p> Um das Filtern zu erleichtern liefert dieses {@link Predicate} alle 
	 * {@link TrainCategory}s, die nicht dem übergebenen Verwendungszweck
	 * entsprechen.
	 */
	private class TrainUsagePredicate implements Predicate<TrainCategory>{
		private String usage;
		
		/**
		 * Standardkonstruktor.
		 * @param usage der Verwendungszweck, z.B. {@literal PASSENGER}, nach
		 *		dem gefiltert werden soll
		 */
		public TrainUsagePredicate(String usage){
			super();
			
			if (usage == null){
				throw new IllegalArgumentException("You need to specify a usage to filter by!");
			}
			if (usage.isEmpty()){
				throw new IllegalArgumentException("Empty usage is not allowed.");
			}
			else this.usage = usage;
		}
		
		/**
		 * Vergleichsmethode.
		 * @param t Die Kategory, die untersucht wird
		 * @return {@literal true} genau dann, wenn der Verwendungszweck mit dem
		 *		im Konstruktor Angegebenen NICHT übereinstimmt, ansonsten
		 *		{@literal false}.
		 */
		@Override
		public boolean test(TrainCategory t) {
			return !(t.getTrainUsage().equalsIgnoreCase(this.usage));
		}
	}
	
	private TimetableData data;
	private LocalTime time;
	@Autowired private TelegramReceiverController receiver;
	
	public TimetableController(){
		try{
			data=RailML2Data.loadML("2015-04-27_EBL-Regefahrplan-Export.xml");	
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
	}
	
	/**
	 * @return Aktuelle Laborzeit (falls verfügbar)
	 */
	public LocalTime getTime(){
		return time;
	}
	
	/**
	 * Setzen der aktuellen Laborzeit
	 * @param time
	 */
	public void setTime(LocalTime time){
		this.time=time;
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

			default:return "Connecting";
		}
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
	 * Liefert alle {@link TrainCategory}s, die einen bestimmten
	 * Verwendungszweck erfüllen.
	 * 
	 * @param use der zu erfüllende Verwendungszweck.
	 * @return eine (ggf. leere) Liste von {@link TrainCategory}s
	 */
	public List<TrainCategory> getTrainCategories(String use){
		List<TrainCategory> ret = new ArrayList<>(data.getTrainCategories());
		ret.removeIf(new TrainUsagePredicate(use));
		return ret;
	}
	
	/**
	 * Liefert eine {@link TrainCategory} anhand ihrer ID.
	 * @param id die ID, nach der gesucht wird
	 * @return die gesuchte {@link TrainCategory} oder {@literal null}, falls 
	 *		keine passende gefunden wurde
	 */
	public TrainCategory getTrainCategoryById(String id){
		if (id == null){
			throw new IllegalArgumentException("Accessing TrainCategory with ID null!");
		}
		
		for (TrainCategory tc : data.getTrainCategories()){
			if (tc.getId().equals(id)){
				return tc;
			}
		}
		
		return null;
	}
	

	/**
	 * @see Station.getStops
	 * @param station
	 * @return Alle Halte eines Bahnhofs, wenn station nicht null ist. 
	 * Falls station null ist, gibt die Funktion eine leere Liste zurück.
	 */
	public List<Stop> getStopsByStation(Station station){
		if(station==null){
			return new ArrayList<Stop>();
		}
		return station.getStops();
	}
	
	/**
	 * Gibt alle Zugläufe eines Bahnhofs aus
	 * @param station
	 * @param type Je nach {@link FilterType} gibt die Funktion alle Zugläufe, 
	 * ein- oder ausfahrende Zugläufe an.
	 * @return Alle Zugläufe eines Bahnhofs, sofern station nicht null ist 
	 * (sonst leere Liste)
	 */
	public Set<TrainRoute> getTrainRoutesByStation(Station station, FilterType type){
		if(station==null){
			return new HashSet<TrainRoute>();
		}
		
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
	
	
			
	public List<TrainRoute> filter(Iterable<TrainRoute> listToFilter, Station station, LocalTime from, LocalTime to, FilterType type,FilterTime filterTime){
		/* 
		 * Filtert die gegebene Liste nach der Ankunfts-/Abfahrtszeit in der Zeit von [from] bis [to] am angegebenen Bahnhof
		 * und gibt alle entsprechenden "Stop"-Objekte, die mit diesem Bahnhof assoziiert sind, zurück
		 * TODO: Testen (ist garantiert voller Fehler...)
		 */
		if(station==null) {
			return new ArrayList<>();
		}
		
		List<TrainRoute> newList=new ArrayList<>();
		for(TrainRoute route : listToFilter){
			for(Stop stop : route.getStops()){
				if(stop.getStation() == station){
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
					}
				
					//hier passiert der eigentliche Vergleich
					//Die equals sind laut Test ebenfalls notwendig!
					if(stopTime!=null && from!=null && to!=null){
						if((stopTime.isAfter(from) || stopTime.equals(from)) && (stopTime.isBefore(to) || stopTime.equals(to))){
							newList.add(route);
						}
					}
				}
			}
			
		}
		return newList;	
	}

	@EventListener
	public void forwardTelegram(TelegramParsedEvent event) {
		Telegram telegram = event.getSource();
		
		if(telegram instanceof LabTimeTelegram){
			setTime(((LabTimeTelegram) telegram).getTime());
		}
		
		if(telegram instanceof TrainRouteTelegram){
			
		}
		
	}
		
}

