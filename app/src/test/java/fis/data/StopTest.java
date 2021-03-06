/* Copyright 2016 Eric Schölzel, Robert Mörseburg, Zdravko Yanakiev, Jonas Schenke, Oliver Schmidt
 *
 * This file is part of FIS.
 *
 * FIS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FIS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FIS.  If not, see <http://www.gnu.org/licenses/>.
 */
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
	
	@Before
	public void setUp() throws Exception{
		time1=LocalTime.of(0,2);
		time2=LocalTime.of(0, 3);
		track1="1";
		s1=new Station("1","s1");
		s2=new Station("2","s2");
		s3=new Station("3","s3");
		stop1=new Stop(s1,StopType.STOP,time1,time2,track1, 42);
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
		assertEquals("Die MessageId stimmt nicht überein!", 42, stop1.getMessageId());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullConstructor_1(){
		new Stop(null,StopType.STOP,time1,time2,track1,0);
		fail("Konstruktor soll bei Argumenten, die nicht null sein dürfen, entsprechende Exception werfen!");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullConstructor_2(){
		new Stop(s1,null,time1,time2,track1,0);
		fail("Konstruktor soll bei Argumenten, die nicht null sein dürfen, entsprechende Exception werfen!");
	}
	
	@Test
	public void testUpdateActualArrival(){
		LocalTime t=LocalTime.of(3, 3);	
		stop1.updateArrival(t);
		assertEquals("Aktualisierung der Ankunftszeit funktioniert nicht.",stop1.getActualArrival(),t);
	}
	
	/*
	@Test(expected=IllegalArgumentException.class)
	public void testUpdateActualArrival_null(){	
		stop1.updateArrival(null);
		fail("Update mit null darf nicht möglich sein!");
	}
	*/
	
	@Test
	public void testUpdateActualDeparture(){
		LocalTime t=LocalTime.of(3, 3);
		
		stop1.updateDeparture(t);
		assertEquals("Aktualisierung der Ankunftszeit funktioniert nicht.",stop1.getActualDeparture(),t);
	}
	
	/*@Test(expected=IllegalArgumentException.class)
	public void testUpdateActualDeparture_null(){	
		stop1.updateDeparture(null);
		fail("Update mit null darf nicht möglich sein!");
	}*/
	
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
		int message=42;
		stop1.updateMessage(message);
		assertEquals("Aktualisierung der Nachricht funktioniert nicht",stop1.getMessageId(),message);
	}
	
	@Test
	public void testDelay(){
		Stop testStop = new Stop(s1, StopType.STOP, LocalTime.of(1,0),LocalTime.of(1, 5),"1", 42);
		testStop.updateArrival(LocalTime.of(1,3,50));
		testStop.updateDeparture(LocalTime.of(2, 0));
		
		assertEquals("Die ausgegebene Verspätung der Ankunft stimmt nicht!",230,testStop.getDelayArrival());
		assertEquals("Die ausgegebene Verspätung der Abfahrt stimmt nicht!",3300,testStop.getDelayDeparture());
		
		testStop.updateArrival(LocalTime.of(0,50,0));
		testStop.updateDeparture(LocalTime.of(1, 0));
		
		assertEquals("Die ausgegebene Verspätung soll negativ sein, wenn verfüht!",-600,testStop.getDelayArrival());
		assertEquals("Die ausgegebene Verspätung soll negativ sein, wenn verfüht!",-300,testStop.getDelayDeparture());
		
		
		testStop.setActualArrivalNextDay(true);
		testStop.setActualDepartureNextDay(true);
		testStop.updateArrival(LocalTime.of(0, 5));
		testStop.updateDeparture(LocalTime.of(1, 10));
		assertEquals("Die ausgegebene Verspätung der Ankunft auf den nächsten Tag stimmt nicht!", 83100, testStop.getDelayArrival());
		assertEquals("Die ausgegebene Verspätung der Abfahrt auf den nächsten Tag stimmt nicht!", 86700, testStop.getDelayDeparture());	
	
		
	}
	
	@Test
	public void testNullDelay(){
		Stop testStop = new Stop(s1, StopType.STOP, null,null,"1", 42);
		assertEquals("Wenn geplante oder tatsächliche Zeit null ist, soll 0 zurückgegeben werden",0,testStop.getDelayArrival());
		assertEquals("Wenn geplante oder tatsächliche Zeit null ist, soll 0 zurückgegeben werden",0,testStop.getDelayDeparture());
		testStop.updateArrival(LocalTime.MAX);
		testStop.updateDeparture(LocalTime.MAX);
		assertEquals("Wenn geplante oder tatsächliche Zeit null ist, soll 0 zurückgegeben werden",0,testStop.getDelayArrival());
		assertEquals("Wenn geplante oder tatsächliche Zeit null ist, soll 0 zurückgegeben werden",0,testStop.getDelayDeparture());
		
		Stop testStop2 = new Stop(s1, StopType.STOP, LocalTime.MAX,LocalTime.MAX,"1", 42);
		testStop2.updateArrival(null);
		testStop2.updateDeparture(null);
		assertEquals("Wenn geplante oder tatsächliche Zeit null ist, soll 0 zurückgegeben werden",0,testStop.getDelayArrival());
		assertEquals("Wenn geplante oder tatsächliche Zeit null ist, soll 0 zurückgegeben werden",0,testStop.getDelayDeparture());
		
	}
}
