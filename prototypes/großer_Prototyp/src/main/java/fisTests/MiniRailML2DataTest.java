package fisTests;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import fis.*;


public class MiniRailML2DataTest {
	Fahrplan fahrplan;
	
	public MiniRailML2DataTest(){
		
	}
	@Before
	public void setUp(){
		fahrplan=new Fahrplan();
	}
	
	@Test
	public void TestGetStationByID(){
		fahrplan.getData();
		assertEquals("Dornbach",fahrplan.getData().getStationByID("ocp_ID").getName());
	}
	
	@Test
	public void TestDeparture(){
		//assertEquals("09:25:18",fahrplan.getData().getZuglaeufe().get(0).getStops().get(0).getAbfahrtIst().toString());
		fahrplan.getData().getZuglaeufe().get(0).getStops().get(0).getAbfahrtIst().toString();
	}
	
}
