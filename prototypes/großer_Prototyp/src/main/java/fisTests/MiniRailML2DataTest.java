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
		assertEquals("08:33:18",timetable.getData().getTrainRoutes().get(0).getStops().get(0).getAbfahrtIst().toString());
	}
	
	//@Test
	//public void Test
	
}
