package fis.data;

import java.time.LocalTime;

/**
 * Repräsentiert einen spezifischen Halt
 * @author Eric
 *
 */
public class Stop {
	private Station station;
	private LocalTime scheduledDeparture;
	private LocalTime scheduledArrival;
	private LocalTime actualArrival;
	private LocalTime actualDeparture;
	
	private String scheduledTrack;
	private String actualTrack;
	private String message; //Hier bin ich noch unsicher, was dort genau rein soll
	private StopType stopType;
	private TrainRoute trainRoute;

	/**
	 * Erzeugt einen neuen Halt
	 * @param station Der Bahnhof, an dem der Zug hält
	 * @param stopType {@link StopType} dieses Haltes
	 * @param scheduledArrival Geplante Ankunftszeit des Zuges
	 * @param scheduledDeparture Geplante Abfahrtszeit des Zuges.
	 * @param scheduledTrack Geplanter Gleis, an dem der Zug halten soll
	 */
	public Stop(Station station,StopType stopType,LocalTime scheduledArrival, LocalTime scheduledDeparture, String scheduledTrack){
		this.station=station;
		this.stopType=stopType;
		this.scheduledArrival=scheduledArrival;
		this.scheduledDeparture=scheduledDeparture;
		this.scheduledTrack=scheduledTrack;	
		
		this.actualArrival=scheduledArrival;
		this.actualDeparture=scheduledDeparture;
		this.actualTrack=scheduledTrack;
		
		this.message="";
		
		station.addStop(this);	
	}
	
	/**
	 * Getter für station
	 * @return Den Bahnhof {@link Station}, an dem dieser Halt erfolgt.
	 */
	public Station getStation(){
		return station;
		}
	
	/**
	 * Setzt den Link auf den Zuglauf {@link TrainRoute}, zu der dieser Halt gehört.
	 * @param route Der Zuglauf {@link TrainRoute}, der diesen Halt beinhaltet.
	 */
	public void setTrainRoute(TrainRoute route){
		this.trainRoute=route;
	}
	
	/**
	 * Entfernt die Verlinkung dieses Halts zum bisherigen Zuglauf {@link TrainRoute}
	 * Aufrufen, wenn dieser Halt entfernt werden soll (für Datenkonsistenz). Wird im Normalfall automatisch durch {@link Station.removeStop} oder {@link TrainRoute.removeStop} ausgeführt.
	 */
	public void deleteStop(){
		if(trainRoute!=null){
			trainRoute.getStops().remove(this);
		}
		
		if(station!=null){
			station.getStops().remove(this);
		}
		
	}

	/**
	 * Getter für stopType
	 * @return {@link StopType} dieses Haltes
	 */
	public StopType getStopType(){
		return stopType;
	}
	
	
	/**
	 * Aktualisiert den {@link StopType}
	 * @param newStopType Der neue {@link StopType}
	 */
	public void updateStopType(StopType newStopType){
		this.stopType=newStopType;
	}
	
	/**
	 * Aktualisiert die tatsächliche Ankunftszeit des Zuges
	 * @param actualArrival Die neue tatsächliche Ankunftszeit.
	 */
	public void updateArrival(LocalTime actualArrival){
		this.actualArrival=actualArrival;
	}
	
	/**
	 * Aktualisiert die tatsächliche Abfahrtszeit des Zuges
	 * @param actualDeparture Die neue tatsächliche Abfahrtszeit
	 */
	public void updateDeparture(LocalTime actualDeparture){
		this.actualDeparture=actualDeparture;
	}
	
	
	/**
	 * Aktualisiert den tatsächlichen Gleis, an de, der Zug hält.
	 * @param actualTrack Der neue Gleis.
	 */
	public void updateTrack(String actualTrack){
		this.actualTrack=actualTrack;
	}
	
	/**
	 * Aktualisiert die aktuelle Meldung
	 * @param message
	 */
	public void updateMessage(String message){
		this.message=message;
	}
	
	/**
	 * @return Die aktuelle "besondere Nachricht"/Information des Haltes
	 */
	public String getMessage(){
		return message;
	}
	
	
	/**
	 * @return Geplante Abfahrtszeit
	 */
	public LocalTime getScheduledDeparture(){return scheduledDeparture;}
	
	/** 
	 * @return Geplante Ankunftszeit
	 */
	public LocalTime getScheduledArrival(){return scheduledArrival;}
	
	/**
	 * @return Tatsächliche Abfahrtszeit
	 */
	public LocalTime getActualDeparture(){return actualDeparture;}
	
	/**
	 * @return Tatsächliche Ankunftszeit
	 */
	public LocalTime getActualArrival(){return actualArrival;}
	
	/**
	 * @return Geplanter Gleis
	 */
	public String getScheduledTrack(){return scheduledTrack;}
	
	/**
	 * @return Tatsächlicher Gleis
	 */
	public String getActualTrack(){return actualTrack;}
	
	/**
	 * @return Der Zuglauf {@link TrainRoute}, der diesen Halt beinhaltet (muss natürlich vorher gesetzt worden sein!)
	 */
	public TrainRoute getTrainRoute(){
		return trainRoute;
	}

	public void printDebugInformation(){
		//erstmal nur zum Testen
		System.out.println("---");
		if(station!=null){
			System.out.println("Station: "+station.getName());
		} else {
			System.out.println("!!STATION: NULL!!");
		}
		System.out.println("Train Number: ");
		System.out.println("Scheduled Arrival: "+scheduledArrival);
		System.out.println("Scheduled Departure: "+scheduledDeparture);
		System.out.println("Scheduled Track: "+scheduledTrack);
	}
	
	
	
}
