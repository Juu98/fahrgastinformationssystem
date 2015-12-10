package fis.data;

import static org.junit.Assert.*;

import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

public class StopTest {
	Station s1,s2,s3;
	Stop stop1;
	LocalTime time1;
	LocalTime time2;
	String track1;
	
	//TODO: Die Funktionen zur TrainRoute-Verlinkung in seperatem Datenkonsistenz-Test testen!
	
	@Before
	public void setUp() throws Exception{
		time1=LocalTime.of(0,2);
		time2=LocalTime.of(0, 3);
		track1="1";
		s1=new Station("1","s1");
		s2=new Station("2","s2");
		s3=new Station("3","s3");
		stop1=new Stop(s1,StopType.STOP,time1,time2,track1);
	}
	@Test
	public void testGetter(){
		assertEquals("Bahnhof stimmt nicht überein!", stop1.getStation(),s1);
		assertEquals("Geplante Ankunftszeit stimmt nicht",stop1.getScheduledArrival(),time1);
		assertEquals("Tatsächliche Ankunftszeit muss hier gleich der geplanten Ankunftszeit sein!",stop1.getScheduledArrival(),stop1.getActualArrival());
		assertEquals("Geplante Abfahrtszeit stimmt nicht",stop1.getScheduledDeparture(),time2);
		assertEquals("Tatsächliche Abfahrtszeit muss hier gleich der geplanten Abfahrtszeit sein!",stop1.getScheduledDeparture(),stop1.getActualDeparture());
		assertEquals("Geplanter Gleis stimmt nicht",stop1.getScheduledTrack(),track1);
		assertEquals("Tatsächlicher Gleis muss hier gleich dem geplanten Gleis sein!",stop1.getScheduledTrack(),stop1.getActualTrack());
		assertEquals("StopType stimmt nicht!", stop1.getStopType(),StopType.STOP);
		
		assertEquals("Message sollte noch ein leerer String sein!", stop1.getMessage(),"");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullConstructor_1(){
		new Stop(null,StopType.STOP,time1,time2,track1);
		fail("Konstruktor soll bei Argumenten, die nicht null sein dürfen, entsprechende Exception werfen!");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullConstructor_2(){
		new Stop(s1,null,time1,time2,track1);
		fail("Konstruktor soll bei Argumenten, die nicht null sein dürfen, entsprechende Exception werfen!");
	}
	
	
	@Test
	public void testUpdateActualArrival(){
		LocalTime t=LocalTime.of(3, 3);	
		stop1.updateArrival(t);
		assertEquals("Aktualisierung der Ankunftszeit funktioniert nicht.",stop1.getActualArrival(),t);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUpdateActualArrival_null(){	
		stop1.updateArrival(null);
		fail("Update mit null darf nicht möglich sein!");
	}
	
	@Test
	public void testUpdateActualDeparture(){
		LocalTime t=LocalTime.of(3, 3);
		
		stop1.updateDeparture(t);
		assertEquals("Aktualisierung der Ankunftszeit funktioniert nicht.",stop1.getActualDeparture(),t);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUpdateActualDeparture_null(){	
		stop1.updateDeparture(null);
		fail("Update mit null darf nicht möglich sein!");
	}
	
	@Test
	public void testUpdateTrack(){
		String track="42";
		stop1.updateTrack(track);
		assertEquals("Aktualisierung des Gleises funktioniert nicht",stop1.getActualTrack(),track);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUpdateTrack_null(){	
		stop1.updateTrack(null);
		fail("Update mit null darf nicht möglich sein!");
	}
	
	@Test
	public void testUpdateStopType(){
		stop1.updateStopType(StopType.STOP);
		assertEquals("Aktualisierung des StopTypes funktioniert nicht",stop1.getStopType(),StopType.STOP);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUpdateStopType_null(){	
		stop1.updateStopType(null);
		fail("Update mit null darf nicht möglich sein!");
	}
	
	@Test
	public void testUpdateMessage(){
		String message="42";
		stop1.updateMessage(message);
		assertEquals("Aktualisierung der Nachricht funktioniert nicht",stop1.getMessage(),message);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUpdateMessage_null(){	
		stop1.updateMessage(null);
		fail("Update mit null darf nicht möglich sein!");
	}
	
	
	
	
	
	
	
}
