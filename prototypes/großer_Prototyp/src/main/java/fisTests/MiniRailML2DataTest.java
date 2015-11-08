package fisTests;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import fis.*;

public class MiniRailML2DataTest {
	Timetable timetable;
	
	public MiniRailML2DataTest(){
		
	}
	@Before
	public void setUp(){
		timetable=new Timetable();
	}
	
	@Test
	public void TestGetStationByID(){
		timetable.getData();
		assertEquals("Dornbach",timetable.getData().getStationByID("ocp_ID").getName());
	}
	
	@Test
	public void TestDeparture(){
		assertEquals("Abfahrt bei Endbahnhöfen soll intern null sein", null, timetable.getData().getTrainRoutes().get(1).getStops().get(6).getActualDeparture());
		assertEquals("Abfahrt bei Endbahnhöfen soll intern null sein", null, timetable.getData().getTrainRoutes().get(1).getStops().get(6).getScheduledDeparture());
				
		assertEquals("Abfahrtszeit stimmt nicht überein. ScheduledDeparture und ActualDeparture müssen beim RailML-Offline-Fahrplan außerdem übereinstimmen","08:33:18",timetable.getData().getTrainRoutes().get(0).getStops().get(0).getActualDeparture().toString());
		assertEquals("Abfahrtszeit stimmt nicht überein. ScheduledDeparture und ActualDeparture müssen beim RailML-Offline-Fahrplan außerdem übereinstimmen","08:33:18",timetable.getData().getTrainRoutes().get(0).getStops().get(0).getScheduledDeparture().toString());
	}
	
	@Test
	public void TestArrival(){	
		assertEquals("Ankunft bei Startbahnhöfen soll intern null sein",null,timetable.getData().getTrainRoutes().get(3).getStops().get(0).getActualArrival());
		assertEquals("Ankunft bei Startbahnhöfen soll intern null sein",null,timetable.getData().getTrainRoutes().get(3).getStops().get(0).getScheduledArrival());
				
		//Testen einer 'echten' Ankunftszeit
		//ScheduledArrival und ActualArrival müssen beim RailML-Offline-Fahrplan übereinstimmen
		String failureMsg="Ankuftszeit stimmt nicht überein. ScheduledArrival und ActualArrival müssen beim RailML-Offline-Fahrplan außerdem übereinstimmen";
		assertEquals(failureMsg,"10:38:18",timetable.getData().getTrainRoutes().get(2).getStops().get(1).getActualArrival().toString());
		assertEquals(failureMsg,"10:38:18",timetable.getData().getTrainRoutes().get(2).getStops().get(1).getScheduledArrival().toString());
		
	}
	
}
