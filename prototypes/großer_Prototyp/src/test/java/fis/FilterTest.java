package fis;
import static org.junit.Assert.assertEquals;

import java.time.LocalTime;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FilterTest {
	Timetable timetable;
	
	public FilterTest(){
		
	}
	@Before
	public void setUp(){
		System.out.println("\n -- FilterTest >Initialize< -- \n");
		timetable=new Timetable();
		
		System.out.println("\n -- FilterTest >Start Test< -- \n");
	}
	
	@Test
	public void TestFilter(){
		LocalTime from=LocalTime.of(10, 51, 18);
		LocalTime to=LocalTime.of(14, 51, 18);
		List<Stop> stopList=timetable.filterByTime(timetable.getData().getTrainRoutes(), timetable.getData().getStationByID("ocp_ID"), from, to, FilterType.Departure, FilterTime.Actual);
		assertEquals("Der Filter muss EINSCHLIESSLICH der [from]-Zeit filtern",from,stopList.get(0).getScheduledDeparture());
		assertEquals("Der Filter muss EINSCHLIESSLICH der [to]-Zeit filtern",to,stopList.get(stopList.size()-1).getScheduledDeparture());
		
		for(Stop stop:stopList){
			stop.printDebugInformation();
		}
	}
	
	@After
	public void End(){
		System.out.println(" \n -- Filter Test >End< -- \n");
	}
}
