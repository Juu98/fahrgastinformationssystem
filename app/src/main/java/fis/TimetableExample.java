/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fis;

import java.time.LocalTime;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

import fis.data.Station;
import fis.data.Stop;
import fis.data.StopType;
import fis.data.TimetableController;
import fis.data.TimetableData;
import fis.data.TrainRoute;

/**
 * Example Timetable to generate some output.
 * @author Robert
 */
//@Component
public class TimetableExample extends TimetableController {
	private final String STATE = "online";
	private TimetableData data;
	
	public TimetableExample(){
		this.data = new TimetableData();
		
		// Bahnhöfe
		Station hbf = new Station("HBF", "Dresden-Hauptbahnhof");
		Station pir = new Station("PIR", "Pirna");
		Station bsc = new Station("BSC", "Bad Schandau");
		Station mei = new Station("MEI", "Meißen");
		Station neu = new Station("NEU", "Dresden-Neustadt");
				
		data.addStation(hbf);
		data.addStation(pir);
		data.addStation(bsc);
		data.addStation(mei);
		data.addStation(neu);
		
		// Züge
		ArrayList<Stop> s1 = new ArrayList<>();
		s1.add(new Stop(hbf, StopType.begin, null, LocalTime.of(12, 0), "1"));
		s1.add(new Stop(pir, StopType.stop, LocalTime.of(12, 15), LocalTime.of(12, 17), "3"));
		s1.add(new Stop(bsc, StopType.end, LocalTime.of(12, 30), null, "2"));
		
		ArrayList<Stop> s2 = new ArrayList<>();
		s2.add(new Stop(hbf, StopType.begin, null, LocalTime.of(12, 10), "2"));
		s2.add(new Stop(mei, StopType.end, LocalTime.of(12, 45), null, "1"));
				
		data.addTrainRoute(new TrainRoute("S1", 1, null, s1));
		data.addTrainRoute(new TrainRoute("S2", 2, null, s2));
	}
	
	@Override
	public TimetableData getData(){
		return data;
	}
	
	@Override
	public LocalTime getTime(){
		return LocalTime.now();
	}
	
	
}
