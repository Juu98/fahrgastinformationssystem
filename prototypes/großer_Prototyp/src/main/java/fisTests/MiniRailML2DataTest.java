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
		//assertEquals("09:25:18",fahrplan.getData().getZuglaeufe().get(0).getStops().get(0).getAbfahrtIst().toString());
		timetable.getData().getZuglaeufe().get(0).getStops().get(0).getAbfahrtIst().toString();
	}
	
}
