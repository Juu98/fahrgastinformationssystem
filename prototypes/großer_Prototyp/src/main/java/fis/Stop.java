package fis;

import java.time.LocalTime;

public class Stop {
	private Station station;
	private LocalTime ScheduledDeparture;
	private LocalTime ScheduledArrival;
	private LocalTime ActualArrival;
	private LocalTime ActualDeparture;
	
	private byte ScheduledTrack;
	private byte ActualTrack;
	private String Meldung; //Hier bin ich noch unsicher, was dort genau rein soll
	private StopType stopType;
	
	public Stop(Station station,StopType stopType,LocalTime ScheduledArrival, LocalTime ScheduledDeparture, byte ScheduledTrack){
		this.station=station;
		this.stopType=stopType;
		this.ScheduledArrival=ScheduledArrival;
		this.ScheduledDeparture=ScheduledDeparture;
		this.ScheduledTrack=ScheduledTrack;	
		
		this.ActualArrival=ScheduledArrival;
		this.ActualDeparture=ScheduledDeparture;
		this.ActualTrack=ScheduledTrack;
		
	}
	public Station getStation(){return station;}
	
	public StopType getStopType(){
		return stopType;
	}
	
	public void updateStopType(StopType newStopType){
		this.stopType=newStopType;
	}
	
	public void updateAnkunft(LocalTime ActualArrival){
		this.ActualArrival=ActualArrival;
	}
	
	public void updateAbfahrt(LocalTime ActualDeparture){
		this.ActualDeparture=ActualDeparture;
	}
	
	public void updateGleis(byte ActualTrack){
		this.ActualTrack=ActualTrack;
	}
	
	public LocalTime getScheduledDeparture(){return ScheduledDeparture;}
	public LocalTime getScheduledArrival(){return ScheduledArrival;}
	public LocalTime getActualDeparture(){return ActualDeparture;}
	public LocalTime getActualArrival(){return ActualArrival;}
	public byte getScheduledTrack(){return ScheduledTrack;}
	public byte getActualTrack(){return ActualTrack;}
	
	//public LocalTime getVerspaetung(){
		//Ankunft oder Abfahrtszeit heranziehen??
	//}
	
	public void printDebugInformation(){
		//erstmal nur zum Testen
		System.out.println("---");
		System.out.println("Station: "+station.getName());
		System.out.println("Scheduled Arrival: "+ScheduledArrival);
		System.out.println("Scheduled Departure: "+ScheduledDeparture);
		System.out.println("Scheduled Track: "+ScheduledTrack);
	}
	
}
