package fahrplan;

import java.time.LocalTime;

public class Halt {
	Bahnhof bahnhof;
	LocalTime AbfahrtSoll;
	LocalTime AnkunftSoll;
	LocalTime AnkunftIst;
	LocalTime AbfahrtIst;
	
	byte GleisSoll;
	byte GleisIst;
	String Meldung; //Hier bin ich noch unsicher, was dort genau rein soll
	
	
	public Halt(Bahnhof bahnhof,LocalTime ankunftSoll, LocalTime abfahrtSoll, byte gleisSoll){
		this.bahnhof=bahnhof;
		this.AnkunftSoll=ankunftSoll;
		this.AbfahrtSoll=abfahrtSoll;
		this.GleisSoll=gleisSoll;	
		
		this.AnkunftIst=ankunftSoll;
		this.AbfahrtIst=abfahrtSoll;
		this.GleisIst=gleisSoll;
		
	}
	public Bahnhof getBahnhof(){return bahnhof;}
	
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
