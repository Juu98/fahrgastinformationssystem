package fis.data;

import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TrainRouteTest {
	Station s1,s2,s3;
	Stop stop1,stop2,stop3,stop4,stop5;
	TrainRoute route1,route2;
	List<Stop> stops1;
	TrainCategory cat1;
	
	@Before
	public void setUp() throws Exception{
		cat1=new TrainCategory("1","cat1","cat1_desc","cat1_usage");
		
		s1=new Station("1","s1");
		s2=new Station("2","s2");
		s3=new Station("3","s3");
		
		stop1=new Stop(s1,StopType.BEGIN,null,LocalTime.of(0, 1),"1");
		stop2=new Stop(s2,StopType.STOP,LocalTime.of(0, 2),LocalTime.of(0, 3),"2");
		stop3=new Stop(s3,StopType.END,LocalTime.of(0, 4),null,"3");
		
		stops1=new ArrayList<Stop>();
		stops1.add(stop1);
		stops1.add(stop2);
		stops1.add(stop3);
		
		route1=new TrainRoute("1",1,cat1,stops1);
		
	}
	
	@Test
	public void testConstructorAndGetter(){
		assertEquals("ID der TrainRoute stimmt nicht!", route1.getId(),"1");
		assertEquals("TrainCategory stimmt nicht!", route1.getTrainCategory(), cat1);
		assertEquals("Stops stimmen nicht!", route1.getStops(),stops1);
		assertEquals("TrainNumber stimmt nicht!", route1.getTrainNumber(),1);
	}
	
	@Test
	public void testStopLinked(){
		assertEquals("Die Referenz des Stops auf die TrainRoute wurde nicht korrekt gesetzt!", route1,stop1.getTrainRoute());
	}
}
