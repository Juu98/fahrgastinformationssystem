package fis;

import org.springframework.stereotype.Controller;

@Controller
public class Fahrplan {
	private ConnectionState state;
	
	public Fahrplan(){
		//Versuche, zu connecten
		tryConnect();	
	}
	
	public FahrplanData getData(){
		return state.data;
	}
	
	private void tryConnect(){	
		//hier failt er erstmal automatisch, da Prototyp
		state=new Offline();
		state.initialize();
	}
	
	abstract class ConnectionState{
		FahrplanData data;	
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
