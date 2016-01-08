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
		
		stop1=new Stop(s1,StopType.BEGIN,null,LocalTime.of(0, 1),"1",0);
		stop2=new Stop(s2,StopType.STOP,LocalTime.of(0, 2),LocalTime.of(0, 3),"2",0);
		stop3=new Stop(s3,StopType.END,LocalTime.of(0, 4),null,"3",0);
		
		stops1=new ArrayList<Stop>();
		stops1.add(stop1);
		stops1.add(stop2);
		stops1.add(stop3);
		
		route1=new TrainRoute("1","1",cat1,stops1, 1);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullConstructor_1(){
		new TrainRoute(null,"0",cat1,stops1, 0);
		fail("Konstruktor soll bei null-Argumenten entsprechende Exception werfen!");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullConstructor_2(){
		new TrainRoute("2","0",null,stops1, 0);
		fail("Konstruktor soll bei null-Argumenten entsprechende Exception werfen!");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullConstructor_3(){
		new TrainRoute("2","0",cat1,null, 0);
		fail("Konstruktor soll bei null-Argumenten entsprechende Exception werfen!");
	}
	
	@Test
	public void testGetMessageId(){
		assertEquals("Die MessageId stimmt nicht überein!", 1, route1.getMessageId());
	}
	
	@Test
	public void testGetFirstStop(){
		assertEquals("Es soll der erste Halt (=Starthalt) zurückgegeben werden!",route1.getFirstStop(),stop1);
	}
	
	@Test
	public void testGetLastStop(){
		assertEquals("Es soll der letzte Halt (=Endhalt) zurückgegeben werden!",route1.getLastStop(),stop3);
	}
	
	@Test
	public void testGetter(){
		assertEquals("ID der TrainRoute stimmt nicht!", route1.getId(),"1");
		assertEquals("TrainCategory stimmt nicht!", route1.getTrainCategory(), cat1);
		assertEquals("Stops stimmen nicht!", route1.getStops(),stops1);
		assertEquals("TrainNumber stimmt nicht!", route1.getTrainNumber(),1);
	}
	
	@Test
	public void testStopLinked(){
		assertEquals("Die Referenz des Stops auf die TrainRoute wurde nicht korrekt gesetzt!", route1,stop1.getTrainRoute());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getStopAtStationNullTest(){
		route1.getStopAtStation(null);
	}
	
	@Test
	public void getStopAtStationNonExistantTest(){
		Station notOnRoute = new Station("NOR", "Not On Route");
		assertNull("getStopAtStation should return null if the statoin is not on the route!", route1.getStopAtStation(notOnRoute));
	}
	
	@Test
	public void getStopAtStationTest(){
		assertEquals("The stops should be equal!", route1.getStopAtStation(s1), stop1);
	}
}
