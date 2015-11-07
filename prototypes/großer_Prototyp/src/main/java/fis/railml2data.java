package fis;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.railml.schemas._2009.EOcp;
import org.railml.schemas._2009.EOcpTT;
import org.railml.schemas._2009.ETrainPart;
import org.railml.schemas._2009.Infrastructure;
import org.railml.schemas._2009.Railml;
import org.railml.schemas._2009.TOcpOperationalType;
import org.railml.schemas._2009.Timetable;


import fis.railmlparser.RailMLParser;

public class railml2data {
	
	public static TimetableData loadML(String path) throws Exception{
		TimetableData data=new TimetableData();
		
		RailMLParser parser=new RailMLParser();
		try{
			Railml railml=parser.parseRailML(path);
			
			Infrastructure infra=railml.getInfrastructure();
			for(EOcp ocp:infra.getOperationControlPoints().getOcp()){
				TOcpOperationalType ocptype=ocp.getPropOperational().getOperationalType();
				if(ocptype==TOcpOperationalType.STATION | ocptype==null){ //Der zweite Teil der Bedingung ist atm noch fragwürdig...
					data.addStation(new Station(ocp.getId(),ocp.getName()));
				}
			}
			
			Timetable timetable=railml.getTimetable();
			for(ETrainPart trainPart:timetable.getTrainParts().getTrainPart()){
				//TODO: Zuggattungen beachten!
				
				List<Stop> stops=new ArrayList<Stop>();
				
				System.out.println("..");
				System.out.println("Zuglauf "+trainPart.getId());
				
				for(EOcpTT ocptt:trainPart.getOcpsTT().getOcpTT()){
					if(!ocptt.getOcpType().equals("pass")){ //auch hier gibts Stopsignale etc; kA ob dieser kleine Hack sinnvoll ist -> nachfragen!
					StopType stopType;
										
					//nur zur Initialisierung
					LocalTime arrival=LocalTime.MIDNIGHT;
					LocalTime departure=LocalTime.MIDNIGHT;
		
					String type=ocptt.getOcpType();
					switch(type){
					case "begin": stopType=StopType.begin; break;
					case "pass": stopType=StopType.pass; break;
					case "stop": stopType=StopType.stop; break;
					default: stopType=StopType.end;
					}			
					
					System.out.println("StopType: "+stopType.toString());
					
					
					//TODO: Hier wird's etwas hässlich, unbedingt überprüfen, ob das funktioniert!
					if(stopType==StopType.stop | stopType==StopType.end){	
						XMLGregorianCalendar calArrival=ocptt.getTimes().get(0).getArrival();		
						arrival=LocalTime.of(calArrival.getHour(), calArrival.getMinute(), calArrival.getSecond());
					}
					
					if(stopType!=StopType.end){			
						XMLGregorianCalendar calDeparture=ocptt.getTimes().get(0).getDeparture();		
						arrival=LocalTime.of(calDeparture.getHour(), calDeparture.getMinute(), calDeparture.getSecond());				
					}
					System.out.println(arrival.toString());
					
					//TODO: Ebenfalls testen!			
					Station station=data.getStationByID(((EOcp)ocptt.getOcpRef()).getId());
					
					byte track=0;
					if(ocptt.getTrackInfo()!=null){
					track=Byte.parseByte(ocptt.getTrackInfo()); 
					System.out.println("Gleis: "+track);
					} else {System.out.println("Gleis: ??");} //warum auch immer manchmal kein Gleis dabei steht...
					
					Stop stop=new Stop(station, stopType, arrival, departure, track);
					stops.add(stop);
				}
				}
				
				int trainNumber=Integer.parseInt(trainPart.getTrainNumber());
				data.addZuglauf(new TrainRoute(trainPart.getId(),trainNumber,TrainType.Regionalzug,stops));
			}
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
		
		return data;
	}
}
