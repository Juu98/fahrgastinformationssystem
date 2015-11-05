package fahrplan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
public class FahrplanController {
	private ConnectionState state;
	
	
	
	abstract class ConnectionState{
		FahrplanData data;
		
		public ConnectionState(){ 
			initialize();
		}
		
		abstract void initialize(); //Laden und initialisieren
	}
	
	class Offline extends ConnectionState {
		boolean isConnecting;
		
		@Override
		void initialize() {
			/* Aufruf XML-Parser;
			 * Laden des Offline-Fallbacks
			 */
			//TODO: Aufruf implemenentieren
		
			//Versuche, zu connecten
			tryConnect();			
			}
	
		void tryConnect(){
			//TODO: Hier versuchen, mit dem Server zu connecten
			//Falls erfolgreich, Statuswechsel
			isConnecting=true;
		}
	}
	
	class Online extends ConnectionState{

		@Override
		@Autowired
		void initialize() {
			// Fahrplantelegram auswerten etc.
			
		}
		
	}
	

}
