package fis;

import org.springframework.stereotype.Controller;



@Controller
public class FahrplanController {
	private ConnectionState state;
	
	public FahrplanController(){
		//Versuche, zu connecten
		tryConnect();	
	}
	
	private void tryConnect(){	
		//hier failt er erstmal automatisch, da Prototyp
		state=new Offline();
	}
	
	abstract class ConnectionState{
		FahrplanData data;	
		public ConnectionState(){ 
			initialize();
		}		
		abstract void initialize(); //Laden und initialisieren
	}
	
	class Offline extends ConnectionState {
		@Override
		void initialize() {
				//Laden der XML
				data=railml2data.loadML("EBL Regefahrplan simple.xml");				
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
