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
import java.util.ArrayList;
import java.util.List;

import fis.common.CommonConfig;
import org.junit.Before;
import org.junit.Test;
import fis.telegrams.LabTimeTelegram;
import fis.telegrams.TelegramParsedEvent;
import fis.telegrams.TrainRouteTelegram;
import fis.telegrams.TrainRouteTelegram.StopData;

public class TimetableControllerTest{
	TimetableController timetable;
	TimetableData data;
	Station s1,s2,s3,s4;
	Stop stop1,stop2,stop3,stop4,stop5, stop1_new, stop2_new, stop3_new;
	TrainRoute route1,route2, route1_new;
	TrainCategory cat1;
	
	
	@Before
	public void setUp() throws Exception{
		CommonConfig conf = new CommonConfig();
		timetable=new TimetableController(conf);
		timetable.resetData();
		Message m1 = new Message();
		m1.setIndex(1);
		m1.setMessage("Test Message 1");
		timetable.getMessageMap().put(1, m1);
		Message m2 = new Message();
		m2.setIndex(2);
		m2.setMessage("Test Message 2");
		timetable.getMessageMap().put(2, m2);
		
		data=timetable.getData();
		
		if(data==null) throw new Exception("data should not be null!");
		
		cat1=new TrainCategory("1","cat1","cat1_desc","cat1_usage");
		
		s1=new Station("1","s1");
		s2=new Station("2","s2");
		s3=new Station("3","s3");
		s4=new Station("4","s4");
		
		stop1=new Stop(s1,StopType.BEGIN,null,LocalTime.of(0, 1),"1", 1);
		stop2=new Stop(s2,StopType.STOP,LocalTime.of(0, 2),LocalTime.of(0, 3),"2",0);
		stop3=new Stop(s3,StopType.END,LocalTime.of(0, 4),null,"3",0);
		
		List<Stop> stops1=new ArrayList<Stop>();
		stops1.add(stop1);
		stops1.add(stop2);
		stops1.add(stop3);
		
		route1=new TrainRoute("1","1",cat1,stops1, 2);
		
		List<Stop> stops2=new ArrayList<Stop>();
		stop4=new Stop(s2,StopType.BEGIN,null,LocalTime.of(3,1),"4",0);
		stop5=new Stop(s1,StopType.END,LocalTime.of(3, 1),null,"5",0);
		stops2.add(stop4);
		stops2.add(stop5);
		
		route2=new TrainRoute("2","2",cat1,stops2, 0);
		
		data.addTrainCategory(cat1);
		data.addStation(s1);
		data.addStation(s2);
		data.addStation(s3);
		
		data.addTrainRoute(route1);
		data.addTrainRoute(route2);
		
		
		
		List<Stop> newStops=new ArrayList<Stop>();
		
		stop1_new=new Stop(stop1.getStation(),stop1.getStopType(),stop1.getScheduledArrival(),stop1.getScheduledDeparture(),stop1.getScheduledTrack(),1);
		stop2_new=new Stop(s4,StopType.STOP,LocalTime.of(0,2,30),LocalTime.of(0,2,45),"4",5);
		stop3_new=new Stop(stop3.getStation(),stop3.getStopType(),stop3.getScheduledArrival(),stop3.getScheduledDeparture(),stop3.getScheduledTrack(),10);
		
		newStops.add(stop1_new);
		newStops.add(stop2_new);
		newStops.add(stop3_new);
		
		
		route1_new=new TrainRoute(route1.getId(),"999",cat1,newStops, 2);
	}	

	
	
	@Test
	public void testGetStopsByStation(){
		if(timetable.getStopsByStation(null)==null) fail("Wenn station null ist, soll eine leere Liste zurückgegeben werden.");
		
		List<Stop> testList=timetable.getStopsByStation(s1);
		assertTrue( "GetStopsByStation enthält nicht alle Stops des Bahnhofs.",testList.contains(stop1));
		assertTrue( "GetStopsByStation enthält nicht alle Stops des Bahnhofs.",testList.contains(stop5));
	}
	
	@Test
	public void testUpdateTrainRoute_alreadyExists(){
		String oldId=route1.getId();
		
		timetable.updateTrainRoute(route1_new);
		
		boolean found=false;
		for(TrainRoute route:timetable.getTrainRoutes()){
			if(route.getId().equals(oldId)){
				found=true;
				assertEquals("Zugnummer der Route wurde nicht richtig aktualisiert","999",route.getTrainNumber());
				assertEquals("Halte wurden nicht richtig aktualisiert",stop1_new,route.getStops().get(0));
				assertEquals("Halte wurden nicht richtig aktualisiert",stop2_new,route.getStops().get(1));
				assertEquals("Halte wurden nicht richtig aktualisiert",stop3_new,route.getStops().get(2));
				assertEquals("TrainCategory wurde nicht richtig aktualisiert",route1_new.getTrainCategory(),route.getTrainCategory());
				
			} 
		}
		if(timetable.getTrainRoutes().contains(route1_new)){
			fail("Es sollte keine neue TrainRoute hinzugefügt, sondern nur die alte modifiziert werden!");
		}
		if(found==false){
			fail("Der Zuglauf mit der entsprechenden Id sollte immer noch vorhanden sein!");
		}
		
		if(timetable.getStopsByStation(stop1_new.getStation()).contains(stop1)){
			fail("Die entsprechende Station sollte nicht mehr mit dem alten Stop verlinkt sein, wenn dieser im neuen Zuglauf nicht mehr existiert!");
		}
		if(timetable.getStopsByStation(stop2_new.getStation()).contains(stop2)){
			fail("Die entsprechende Station sollte nicht mehr mit dem alten Stop verlinkt sein, wenn dieser im neuen Zuglauf nicht mehr existiert!");
		}
		if(timetable.getStopsByStation(stop3_new.getStation()).contains(stop3)){
			fail("Die entsprechende Station sollte nicht mehr mit dem alten Stop verlinkt sein, wenn dieser im neuen Zuglauf nicht mehr existiert!");
		}
		
	}
	@Test
	public void testGetMessage_Id_Exists(){
		assertEquals("Die erwartete Message stimmt nicht!", "Test Message 1", timetable.getMessage(1));
	}
	
	@Test
	public void testGetMessage_Id_WrongId(){
		assertEquals("Bei einer ID mit nicht existierender Message soll ein leerer String zurückgegeben werden", "", timetable.getMessage(5));
	}
	
	@Test
	public void testGetMessage_Stop_Exists(){
		assertEquals("Die erwartete Message stimmt nicht!", "Test Message 1", timetable.getMessage(stop1));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetMessage_Stop_Null(){
		Stop nullStop = null;
		assertEquals("Bei einem Null-Argument soll ein Fehler geworfen werden!", "", timetable.getMessage(nullStop));
	}
	
	@Test
	public void testGetMessage_TrainRoute_Exists(){
		assertEquals("Die erwartete Message stimmt nicht!", "Test Message 2", timetable.getMessage(route1));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetMessage_TrainRoute_Null(){
		TrainRoute nullRoute = null;
		assertEquals("Bei einem Null-Argument soll ein Fehler geworfen werden!", "", timetable.getMessage(nullRoute));
	}
	
	@Test
	public void testUpdateTrainRoute_new(){
		TrainRoute routeX=new TrainRoute("23452345", "555", cat1, route1_new.getStops(), 0);
		
		timetable.updateTrainRoute(routeX);
		assertTrue("Eine noch nicht in der Datenstruktur existierende TrainRoute soll hinzugefügt werden!",
				timetable.getTrainRoutes().contains(routeX));
		
		
	}
	
	@Test
	public void testForwardTelegram_LabTimeTelegram(){
		LocalTime time=LocalTime.of(10, 47);
		timetable.forwardTelegram(new TelegramParsedEvent(new LabTimeTelegram(time)));
		
		assertEquals("Forwarden des Laborzeit-Telegrams funktioniert nicht!",time,timetable.getTime());
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateTrainRouteFromTelegram_null(){
		timetable.createTrainRouteFromTelegram(null);
		fail("createTrainRouteFromTelegram darf nicht mit NullTelegram funktionieren!");
	}
	
	@Test
	public void testCreateTrainRouteFromTelegram(){
		String trnNum="5";
		String trnCat="catX";
		int messageID=5;
		
		int scheduledTrack1=1;
		int actualTrack1=3;
		int dispoType1=42;
		int messageId1=3;
		int scheduledTrack2=1;
		int actualTrack2=3;
		int dispoType2=42;
		int messageId2=5;
		int scheduledTrack3=10;
		int actualTrack3=11;
		int dispoType3=0;
		int messageId3=0;
		
		
		List<TrainRouteTelegram.StopData> stops = new ArrayList<>();
		
		StopData data1=new TrainRouteTelegram.StopData(1, null, LocalTime.of(0, 0), null, LocalTime.of(0, 1), scheduledTrack1, actualTrack1, dispoType1, messageId1, true, true);
		StopData data2=new TrainRouteTelegram.StopData(2, LocalTime.of(0, 1), LocalTime.of(1, 0), LocalTime.of(0, 2), LocalTime.of(1, 5), scheduledTrack2, actualTrack2, dispoType2, messageId2, false, false);
		StopData data3=new TrainRouteTelegram.StopData(3, LocalTime.of(3, 5), null, LocalTime.of(3, 10), null, scheduledTrack3, actualTrack3, dispoType3, messageId3, false, false);
		
		stops.add(data1);
		stops.add(data2);
		stops.add(data3);
		
		
		TrainRouteTelegram telegram = new TrainRouteTelegram(
			new TrainRouteTelegram.TrainRouteData(trnNum, trnCat, messageID, stops)
		);
		TrainRoute route=timetable.createTrainRouteFromTelegram(telegram);
		
		assertEquals("TrainRouteID stimmt nicht!",trnNum,route.getId());
		assertEquals("TrainCategory stimmt nicht!", trnCat,route.getTrainCategory().getId());
		assertEquals("Die Anzahl der Stops stimmt nicht",3,route.getStops().size());
	
		
		Stop stop1=route.getStops().get(0);
		assertEquals("Station des ersten Stops stimmt nicht!",s1,stop1.getStation());
		assertEquals("ID der Message des ersten Stops stimmt nicht!",messageId1,stop1.getMessageId());
		assertEquals("StopType des ersten Stops muss BEGIN sein",StopType.BEGIN,stop1.getStopType());
		assertEquals("Der erste Stop muss der TrainRoute zugeordnet sein!",route,stop1.getTrainRoute());
		assertEquals("Die geplante Ankunftszeit des ersten Stops soll null sein",null,stop1.getScheduledArrival());
		assertEquals("Die tatsächliche Ankunftszeit des ersten Stops soll null sein",null,stop1.getActualArrival());
		assertEquals("Die geplante Abfahrtszeit des ersten Stops stimmt nicht!",LocalTime.of(0,0),stop1.getScheduledDeparture());
		assertEquals("Die tatsächliche Abfahrtszeit des ersten Stops stimmt nicht!",LocalTime.of(0, 1),stop1.getActualDeparture());
		assertEquals("Der geplante Gleis des ersten Stops stimmt nicht!",String.valueOf(scheduledTrack1),stop1.getScheduledTrack());
		assertEquals("Der tatsächliche Gleis des ersten Stops stimmt nicht!", String.valueOf(actualTrack1),stop1.getActualTrack());
		assertTrue("Der richtige Bahnhof sollte mit dem ersten Stop verlinkt sein!",s1.getStops().contains(stop1));
		assertEquals("Die Flags für die Verspätung wurden nicht richtig gesetzt!",86460,stop1.getDelayDeparture());
		
		Stop stop2=route.getStops().get(1);
		assertEquals("Station des zweiten Stops stimmt nicht!",s2,stop2.getStation());
		assertEquals("ID der Message des zweiten Stops stimmt nicht!",messageId2,stop2.getMessageId());
		assertEquals("StopType des zweiten Stops muss BEGIN sein",StopType.STOP,stop2.getStopType());
		assertEquals("Der zweite Stop muss der TrainRoute zugeordnet sein!",route,stop2.getTrainRoute());
		assertEquals("Die geplante Ankunftszeit des zweiten Stops stimmt nicht",LocalTime.of(0, 1),stop2.getScheduledArrival());
		assertEquals("Die tatsächliche Ankunftszeit des zweiten Stops stimmt nicht",LocalTime.of(0, 2),stop2.getActualArrival());
		assertEquals("Die geplante Abfahrtszeit des zweiten Stops stimmt nicht!",LocalTime.of(1,0),stop2.getScheduledDeparture());
		assertEquals("Die tatsächliche Abfahrtszeit des zweiten Stops stimmt nicht!",LocalTime.of(1, 5),stop2.getActualDeparture());
		assertEquals("Der geplante Gleis des zweiten Stops stimmt nicht!",String.valueOf(scheduledTrack2),stop2.getScheduledTrack());
		assertEquals("Der tatsächliche Gleis des zweiten Stops stimmt nicht!", String.valueOf(actualTrack2),stop2.getActualTrack());
		assertTrue("Der richtige Bahnhof sollte mit dem zweiten Stop verlinkt sein!",s2.getStops().contains(stop2));
		
		Stop stop3=route.getStops().get(2);
		assertEquals("Station des dritten Stops stimmt nicht!",s3,stop3.getStation());
		assertEquals("ID der Message des dritten Stops stimmt nicht!",messageId3,stop3.getMessageId());
		assertEquals("StopType des dritten Stops muss END sein",StopType.END,stop3.getStopType());
		assertEquals("Der dritte Stop muss der TrainRoute zugeordnet sein!",route,stop3.getTrainRoute());
		assertEquals("Die geplante Ankunftszeit des dritten Stops stimmt nicht",LocalTime.of(3, 5),stop3.getScheduledArrival());
		assertEquals("Die tatsächliche Ankunftszeit des dritten Stops stimmt nicht",LocalTime.of(3, 10),stop3.getActualArrival());
		assertEquals("Die geplante Abfahrtszeit des dritten Stops soll null sein",null,stop3.getScheduledDeparture());
		assertEquals("Die tatsächliche Abfahrtszeit des dritten Stops soll null sein",null,stop3.getActualDeparture());
		assertEquals("Der geplante Gleis des dritten Stops stimmt nicht!",String.valueOf(scheduledTrack3),stop3.getScheduledTrack());
		assertEquals("Der tatsächliche Gleis des dritten Stops stimmt nicht!", String.valueOf(actualTrack3),stop3.getActualTrack());
		assertTrue("Der richtige Bahnhof sollte mit dem dritten Stop verlinkt sein!",s3.getStops().contains(stop3));
	}
}
