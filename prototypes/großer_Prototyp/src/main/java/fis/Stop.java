package fis;

import java.time.LocalTime;

public class Stop {
	private Station station;
	private LocalTime AbfahrtSoll;
	private LocalTime AnkunftSoll;
	private LocalTime AnkunftIst;
	private LocalTime AbfahrtIst;
	
	private byte GleisSoll;
	private byte GleisIst;
	private String Meldung; //Hier bin ich noch unsicher, was dort genau rein soll
	private StopType stopType;
	
	public Stop(Station station,StopType stopType,LocalTime ankunftSoll, LocalTime abfahrtSoll, byte gleisSoll){
		this.station=station;
		this.stopType=stopType;
		this.AnkunftSoll=ankunftSoll;
		this.AbfahrtSoll=abfahrtSoll;
		this.GleisSoll=gleisSoll;	
		
		this.AnkunftIst=ankunftSoll;
		this.AbfahrtIst=abfahrtSoll;
		this.GleisIst=gleisSoll;
		
	}
	public Station getBahnhof(){return station;}
	
	public StopType getStopType(){
		return stopType;
	}
	
	public void updateStopType(StopType newStopType){
		this.stopType=newStopType;
	}
	
	public void updateAnkunft(LocalTime ankunftIst){
		this.AnkunftIst=ankunftIst;
	}
	
	public void updateAbfahrt(LocalTime abfahrtIst){
		this.AbfahrtIst=abfahrtIst;
	}
	
	public void updateGleis(byte gleisIst){
		this.GleisIst=gleisIst;
	}
	
	public LocalTime getAbfahrtSoll(){return AbfahrtSoll;}
	public LocalTime getAnkunftSoll(){return AnkunftSoll;}
	public LocalTime getAbfahrtIst(){return AbfahrtIst;}
	public LocalTime getAnkunftIst(){return AnkunftIst;}
	public byte getGleisSoll(){return GleisSoll;}
	public byte getGleisIst(){return GleisIst;}
	
	//public LocalTime getVerspaetung(){
		//Ankunft oder Abfahrtszeit heranziehen??
	//}
	
}
