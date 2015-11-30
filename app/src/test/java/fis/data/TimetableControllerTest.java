package fis.data;

import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TimetableControllerTest {
	TimetableController timetable;
	TimetableData data;
	Station s1,s2,s3;
	Stop stop1,stop2,stop3,stop4,stop5;
	TrainRoute route1,route2;
	
	
	@Before
	public void setUp() throws Exception{
		timetable=new TimetableController();
		data=timetable.getData();
		
		if(data==null) throw new Exception("data should not be null!");
		
		TrainCategory cat1=new TrainCategory("1","cat1","cat1_desc","cat1_usage");
		
		s1=new Station("1","s1");
		s2=new Station("2","s2");
		s3=new Station("3","s3");
		
		stop1=new Stop(s1,StopType.BEGIN,null,LocalTime.of(0, 1),"1");
		stop2=new Stop(s2,StopType.STOP,LocalTime.of(0, 2),LocalTime.of(0, 3),"2");
		stop3=new Stop(s3,StopType.END,LocalTime.of(0, 4),null,"3");
		
		List<Stop> stops1=new ArrayList<Stop>();
		stops1.add(stop1);
		stops1.add(stop2);
		stops1.add(stop3);
		
		route1=new TrainRoute("1",1,cat1,stops1);
		
		List<Stop> stops2=new ArrayList<Stop>();
		stop4=new Stop(s2,StopType.BEGIN,null,LocalTime.of(3,1),"4");
		stop5=new Stop(s1,StopType.END,LocalTime.of(3, 1),null,"5");
		stops2.add(stop4);
		stops2.add(stop5);
		
		route2=new TrainRoute("2",2,cat1,stops2);
		
		data.addTrainCategory(cat1);
		data.addStation(s1);
		data.addStation(s2);
		data.addStation(s3);
		
		data.addTrainRoute(route1);
		data.addTrainRoute(route2);
	}	
	
	
	
	@Test
	public void testGetStopsByStation(){
		if(timetable.getStopsByStation(null)==null) fail("Wenn station null ist, soll eine leere Liste zurückgegeben werden.");
		
		List<Stop> testList=timetable.getStopsByStation(s1);
		assertTrue( "GetStopsByStation enthält nicht alle Stops des Bahnhofs.",testList.contains(stop1));
		assertTrue( "GetStopsByStation enthält nicht alle Stops des Bahnhofs.",testList.contains(stop5));
	}
	
}
