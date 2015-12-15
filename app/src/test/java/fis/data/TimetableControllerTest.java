package fis.data;

import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import fis.telegrams.LabTimeTelegram;
import fis.telegrams.TelegramParsedEvent;
import fis.telegrams.TrainRouteTelegram;
import fis.telegrams.TrainRouteTelegram.StopData;

public class TimetableControllerTest{
	TimetableController timetable;
	TimetableData data;
	Station s1,s2,s3,s4;
	Stop stop1,stop2,stop3,stop4,stop5, stop1_new, stop2_new, stop3_new;
	TrainRoute route1,route2, route1_new;
	TrainCategory cat1;
	
	
	@Before
	public void setUp() throws Exception{
		timetable=new TimetableController();
		data=timetable.getData();
		
		if(data==null) throw new Exception("data should not be null!");
		
		cat1=new TrainCategory("1","cat1","cat1_desc","cat1_usage");
		
		s1=new Station("1","s1");
		s2=new Station("2","s2");
		s3=new Station("3","s3");
		s4=new Station("4","s4");
		
		stop1=new Stop(s1,StopType.BEGIN,null,LocalTime.of(0, 1),"1",0);
		stop2=new Stop(s2,StopType.STOP,LocalTime.of(0, 2),LocalTime.of(0, 3),"2",0);
		stop3=new Stop(s3,StopType.END,LocalTime.of(0, 4),null,"3",0);
		
		List<Stop> stops1=new ArrayList<Stop>();
		stops1.add(stop1);
		stops1.add(stop2);
		stops1.add(stop3);
		
		route1=new TrainRoute("1",1,cat1,stops1);
		
		List<Stop> stops2=new ArrayList<Stop>();
		stop4=new Stop(s2,StopType.BEGIN,null,LocalTime.of(3,1),"4",0);
		stop5=new Stop(s1,StopType.END,LocalTime.of(3, 1),null,"5",0);
		stops2.add(stop4);
		stops2.add(stop5);
		
		route2=new TrainRoute("2",2,cat1,stops2);
		
		data.addTrainCategory(cat1);
		data.addStation(s1);
		data.addStation(s2);
		data.addStation(s3);
		
		data.addTrainRoute(route1);
		data.addTrainRoute(route2);
		
		
		
		List<Stop> newStops=new ArrayList<Stop>();
		
		stop1_new=new Stop(stop1.getStation(),stop1.getStopType(),stop1.getScheduledArrival(),stop1.getScheduledDeparture(),stop1.getScheduledTrack(),1);
		stop2_new=new Stop(s4,StopType.STOP,LocalTime.of(0,2,30),LocalTime.of(0,2,45),"4",5);
		stop3_new=new Stop(stop3.getStation(),stop3.getStopType(),stop3.getScheduledArrival(),stop3.getScheduledDeparture(),stop3.getScheduledTrack(),10);
		
		newStops.add(stop1_new);
		newStops.add(stop2_new);
		newStops.add(stop3_new);
		
		
		route1_new=new TrainRoute(route1.getId(),999,cat1,newStops);
	}	

	
	
	@Test
	public void testGetStopsByStation(){
		if(timetable.getStopsByStation(null)==null) fail("Wenn station null ist, soll eine leere Liste zurückgegeben werden.");
		
		List<Stop> testList=timetable.getStopsByStation(s1);
		assertTrue( "GetStopsByStation enthält nicht alle Stops des Bahnhofs.",testList.contains(stop1));
		assertTrue( "GetStopsByStation enthält nicht alle Stops des Bahnhofs.",testList.contains(stop5));
	}
	
	@Test
	public void testUpdateTrainRoute_alreadyExists(){
		String oldId=route1.getId();
		
		timetable.updateTrainRoute(route1_new);
		
		boolean found=false;
		for(TrainRoute route:timetable.getTrainRoutes()){
			if(route.getId().equals(oldId)){
				found=true;
				assertEquals("Zugnummer der Route wurde nicht richtig aktualisiert",999,route.getTrainNumber());
				assertEquals("Halte wurden nicht richtig aktualisiert",stop1_new,route.getStops().get(0));
				assertEquals("Halte wurden nicht richtig aktualisiert",stop2_new,route.getStops().get(1));
				assertEquals("Halte wurden nicht richtig aktualisiert",stop3_new,route.getStops().get(2));
				assertEquals("TrainCategory wurde nicht richtig aktualisiert",route1_new.getTrainCategory(),route.getTrainCategory());
				
			} 
		}
		if(found==false){
			fail("Der Zuglauf mit der entsprechenden Id sollte immer noch vorhanden sein!");
		}
		
		if(timetable.getStopsByStation(stop1_new.getStation()).contains(stop1)){
			fail("Die entsprechende Station sollte nicht mehr mit dem alten Stop verlinkt sein, wenn dieser im neuen Zuglauf nicht mehr existiert!");
		}
		if(timetable.getStopsByStation(stop2_new.getStation()).contains(stop2)){
			fail("Die entsprechende Station sollte nicht mehr mit dem alten Stop verlinkt sein, wenn dieser im neuen Zuglauf nicht mehr existiert!");
		}
		if(timetable.getStopsByStation(stop3_new.getStation()).contains(stop3)){
			fail("Die entsprechende Station sollte nicht mehr mit dem alten Stop verlinkt sein, wenn dieser im neuen Zuglauf nicht mehr existiert!");
		}
		
	}
	
	@Test
	public void testUpdateTrainRoute_new(){
		TrainRoute routeX=new TrainRoute("23452345", 555, cat1, route1_new.getStops());
		
		timetable.updateTrainRoute(routeX);
		assertTrue("Eine noch nicht in der Datenstruktur existierende TrainRoute soll hinzugefügt werden!",
				timetable.getTrainRoutes().contains(routeX));
		
		
	}
	
	@Test
	public void testForwardTelegram_LabTimeTelegram(){
		LocalTime time=LocalTime.of(10, 47);
		timetable.forwardTelegram(new TelegramParsedEvent(new LabTimeTelegram(time)));
		
		assertEquals("Forwarden des Laborzeit-Telegrams funktioniert nicht!",time,timetable.getTime());
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateTrainRouteFromTelegram_null(){
		timetable.createTrainRouteFromTelegram(null);
		fail("createTrainRouteFromTelegram darf nicht mit NullTelegram funktionieren!");
	}
	
	@Test
	public void testCreateTrainRouteFromTelegram(){
		String trnNum="5";
		String trnCat="catX";
		int messageID=5;
		List<TrainRouteTelegram.StopData> stops = new ArrayList<>();
		
		StopData data1=new TrainRouteTelegram.StopData(1, null, LocalTime.of(1, 0), null, LocalTime.of(1, 2), 1, 3, 42, 3);
		StopData data2=new TrainRouteTelegram.StopData(2, LocalTime.of(0, 1), LocalTime.of(1, 0), LocalTime.of(0, 2), LocalTime.of(1, 2), 1, 3, 42, 3);
		StopData data3=new TrainRouteTelegram.StopData(3, LocalTime.of(3, 5), null, LocalTime.of(3, 10), null, 10, 11, 0, 0);
		stops.add(data1);
		stops.add(data2);
		stops.add(data3);
		
		
		TrainRouteTelegram telegram = new TrainRouteTelegram(
			new TrainRouteTelegram.TrainRouteData(trnNum, trnCat, messageID, stops)
		);
		
		TrainRoute route=timetable.createTrainRouteFromTelegram(telegram);
		
		assertEquals("TrainRouteID stimmt nicht!",trnNum,route.getId());
		assertEquals("TrainCategory stimmt nicht!", trnCat,route.getTrainCategory().getId());
		
		Stop stop1=route.getStops().get(0);
		assertEquals("Station von Stop 1 stimmt nicht!",s1,stop1.getStation());
		
	}
}
