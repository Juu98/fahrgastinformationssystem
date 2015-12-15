package fis.data;

import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TimetableDataTest {
	
	TimetableData data;
	Station s1,s2,s3;
	Stop stop1,stop2,stop3,stop4,stop5;
	TrainRoute route1,route2;
	TrainCategory cat1;
	@Before
	public void setUp() throws Exception{
	data=new TimetableData();
		
		if(data==null) throw new Exception("data should not be null!");
		
		cat1=new TrainCategory("1","cat1","cat1_desc","cat1_usage");
		
		s1=new Station("1","s1");
		s2=new Station("2","s2");
		s3=new Station("3","s3");
		
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
	}
	
	@Test
	public void testAddStation(){
		Station s=new Station("000","s");
		data.addStation(s);
	
		assertTrue(data.getStations().contains(s));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddNullStation(){	
		data.addStation(null);
		fail("Fehler bei Null-Station muss ausgegeben werden!");
	}
	
	@Test
	public void testAddTrainCategory(){
		TrainCategory cat2=new TrainCategory("2","cat2","cat2_desc","cat2_usage");
		data.addTrainCategory(cat2);
		assertTrue("Hinzufügen von TrainCategory funktioniert nicht!", data.getTrainCategories().contains(cat2));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddNullTrainCategory(){	
		data.addTrainCategory(null);
		fail("Fehler bei Null-TrainCategory muss ausgegeben werden!");
	}
	
	@Test
	public void testAddTrainRoute(){
		TrainRoute route3;
		TrainCategory cat2=new TrainCategory("2","cat2","cat2_desc","cat2_usage");
		List<Stop> stops3=new ArrayList<Stop>();
		Stop stop6=new Stop(s2,StopType.BEGIN,null,LocalTime.of(3,1),"4",0);
		Stop stop7=new Stop(s1,StopType.END,LocalTime.of(3, 1),null,"5",0);
		stops3.add(stop4);
		stops3.add(stop5);
		
		route3=new TrainRoute("2",2,cat2,stops3);
		
		data.addTrainRoute(route3);
		
		assertTrue("Hinzufügen von TrainRoute funktioniert nicht!", data.getTrainRoutes().contains(route3));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddNullTrainRoute(){
		data.addTrainRoute(null);
		fail("Hinzufügen einer Null-TrainRoute darf nicht möglich sein!");
	}
	
	@Test
	public void testGetStationByID_Existing(){
		assertEquals("getStationByID muss die Station mit der gegebenen ID liefern!",data.getStationById("1"),s1);
	}
	
	@Test
	public void testGetStationById_notExisting(){
		assertEquals("getStationByID muss bei einer nicht existierenden ID null zurückgeben!",null,data.getStationById(null));
		assertEquals("getStationByID muss bei einer nicht existierenden ID null zurückgeben!",null,data.getStationById(null));
	}
	
	@Test
	public void testGetTrainCategoryById_Existing(){
		assertEquals("getTrainCategoryByID muss die TrainCategory mit der gegebenen ID liefern!",data.getTrainCategoryById("1"),cat1);
	}
	
	@Test
	public void testGetTrainCategoryById_notExisting(){
		assertEquals("getTrainCategoryById muss bei einer nicht existierenden ID null zurückgeben!",null,data.getTrainCategoryById(null));
		assertEquals("getTrainCategoryById muss bei einer nicht existierenden ID null zurückgeben!",null,data.getTrainCategoryById(null));
	}
	
}
