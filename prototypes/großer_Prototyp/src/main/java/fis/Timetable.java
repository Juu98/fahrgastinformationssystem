package fis;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class Timetable {
	private ConnectionState state;

	
	public Timetable(){
		//Versuche, zu connecten
		tryConnect();	
	}
	
	public LocalTime getTime(){
		return state.getTime();
	}
	
	public TimetableData getData(){
		return state.data;
	}
	
	private void tryConnect(){	
		//hier failt er erstmal automatisch, da Prototyp
		state=new Offline();
		state.initialize();
	}
	
	public String getStateName(){
		return state.getStateName();
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
	
	
	
	public List<Stop> filterByTime(List<TrainRoute> listToFilter, Station station, LocalTime from, LocalTime to, FilterType type,FilterTime filterTime){
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
	
	
	
	abstract class ConnectionState{
		TimetableData data;	
		public ConnectionState(){ 
			
		}		
		abstract void initialize(); //Laden und initialisieren
		
		abstract String getStateName();
		
		abstract LocalTime getTime();
	}
	
	class Offline extends ConnectionState {
		@Override
		void initialize(){
			
				//Laden der XML
			try{
				data=railml2data.loadML("2015-04-27_EBL-Regefahrplan-Export.xml");	
			}
			catch(Exception ex){
				System.out.println(ex.toString());
			}
			
		}
		
		@Override
		String getStateName(){
			return "Offline";
		}

		@Override
		LocalTime getTime() {
			return LocalTime.now();
		}
		
		}

	class Online extends ConnectionState{
		public Online(){ //hier Fahrplantelegram entgegennehmen
			
		}
		
		@Override
		void initialize() {
			// Fahrplantelegram auswerten etc.
			
		}
		
		@Override
		String getStateName(){
			return "Online";
		}

		@Override
		LocalTime getTime() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	
	
	
}
