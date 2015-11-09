package fisTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fis.*;

public class TimetableDataTest {
	Timetable timetable;
	
	public TimetableDataTest(){
		
	}
	@Before
	public void setUp(){
		System.out.println("\n -- TimetableData Test >Initialize< -- \n");
		timetable=new Timetable();
		
		System.out.println("\n -- TimetableData Test >Start Test< -- \n");
	}

	@Test
	public void TestGetStationByID(){
		timetable.getData();
		assertEquals("Dornbach",timetable.getData().getStationByID("ocp_ID").getName());
	}
	
	@Test
	public void testGetTrainCategoryById(){
		System.out.println("Verfügbare TrainCategories:");
		for(TrainCategory cat:timetable.getData().getTrainCategories()){
			System.out.println(cat.getId());
		}
		
		if(timetable.getData().getTrainCategoryById("cat_RB")==null) fail("Die Kategorie mit dieser ID existiert, darf also nicht null sein!");
		assertEquals("Name stimmt nicht!",timetable.getData().getTrainCategoryById("cat_RB").getName(),"RB");
		assertEquals("Beschreibung stimmt nicht!",timetable.getData().getTrainCategoryById("cat_RB").getDescription(),"RegionalBahn");
		assertEquals("TrainUsage stimmt nicht!",timetable.getData().getTrainCategoryById("cat_RB").getTrainUsage(),"PASSENGER");
		
	}
	
	
	@After
	public void End(){
		System.out.println(" \n -- TimetableData Test >End< -- \n");
	}
}
