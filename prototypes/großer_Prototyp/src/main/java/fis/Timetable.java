package fis;

import org.springframework.stereotype.Controller;

@Controller
public class Timetable {
	private ConnectionState state;
	
	public Timetable(){
		//Versuche, zu connecten
		tryConnect();	
	}
	
	public TimetableData getData(){
		return state.data;
	}
	
	private void tryConnect(){	
		//hier failt er erstmal automatisch, da Prototyp
		state=new Offline();
		state.initialize();
	}
	
	abstract class ConnectionState{
		TimetableData data;	
		public ConnectionState(){ 
			
		}		
		abstract void initialize(); //Laden und initialisieren
	}
	
	class Offline extends ConnectionState {
		@Override
		void initialize() {
				//Laden der XML
			try{
				data=railml2data.loadML("EBL Regefahrplan simple.xml");	
			}
			catch(Exception ex){
				System.out.println(ex.toString());
			}
			
			}
		}
	
	
	class Online extends ConnectionState{
		public Online(){ //hier Fahrplantelegram entgegennehmen
			
		}
		
		@Override
		void initialize() {
			// Fahrplantelegram auswerten etc.
			
		}
		
	}
	
	
	
}
