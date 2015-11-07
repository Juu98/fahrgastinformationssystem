package fis;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.railml.schemas._2009.EArrivalDepartureTimes;
import org.railml.schemas._2009.EOcp;
import org.railml.schemas._2009.EOcpTT;
import org.railml.schemas._2009.ETrainPart;
import org.railml.schemas._2009.Infrastructure;
import org.railml.schemas._2009.Railml;
import org.railml.schemas._2009.TOcpOperationalType;
import org.railml.schemas._2009.Timetable;


import fis.railmlparser.RailMLParser;

public class railml2data {
	
	public static FahrplanData loadML(String path){
		FahrplanData data=new FahrplanData();
		
		RailMLParser parser=new RailMLParser();
		try{
			Railml railml=parser.parseRailML(path);
			
			Infrastructure infra=railml.getInfrastructure();
			for(EOcp ocp:infra.getOperationControlPoints().getOcp()){
				if(ocp.getPropOperational().getOperationalType()==TOcpOperationalType.STATION){
					data.addStation(new Bahnhof(ocp.getId(),ocp.getName()));
				}
			}
			
			Timetable timetable=railml.getTimetable();
			for(ETrainPart trainPart:timetable.getTrainParts().getTrainPart()){
				//TODO: Zuggattungen beachten!
				List<Halt> stops=new ArrayList<Halt>();
				
				for(EOcpTT ocptt:trainPart.getOcpsTT().getOcpTT()){
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
					
					//TODO: Hier wird's etwas hässlich, unbedingt überprüfen, ob das funktioniert!
					if(stopType==StopType.stop | stopType==StopType.end){	
						XMLGregorianCalendar calArrival=ocptt.getTimes().get(0).getArrival();		
						arrival=LocalTime.of(calArrival.getHour(), calArrival.getMinute(), calArrival.getSecond());
					}
					
					if(stopType!=StopType.end){			
						XMLGregorianCalendar calDeparture=ocptt.getTimes().get(0).getDeparture();		
						arrival=LocalTime.of(calDeparture.getHour(), calDeparture.getMinute(), calDeparture.getSecond());				
					}
					
					//TODO: Ebenfalls testen!
					Bahnhof station=data.getStationByID(ocptt.getOcpRef().toString());
					if(station==null) System.out.println("ACHTUNG: Bahnhof mit der ID "+ocptt.getOcpRef() + " nicht gefunden!");
					
					byte track=Byte.parseByte(ocptt.getTrackInfo());
					
					Halt stop=new Halt(station, stopType, arrival, departure, track);
					stops.add(stop);
				}
				
				int trainNumber=Integer.parseInt(trainPart.getTrainNumber());
				data.addZuglauf(new Zuglauf(trainPart.getId(),trainNumber,TrainType.Regionalzug,stops));
			}
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
		
		return data;
	}
}
