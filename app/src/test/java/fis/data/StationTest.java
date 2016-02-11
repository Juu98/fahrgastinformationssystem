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

import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class StationTest {
    private Station station;
    private Stop stop1;

    @Before
    public void setUp() throws Exception {
	this.station = new Station("TS", "Test Station");
	this.stop1 = new Stop(station, StopType.STOP, LocalTime.of(13, 37), LocalTime.of(16, 20), "1",0);
    }

    @Test
    public void testGetter(){
    	assertEquals("ID stimmt nicht!","TS",station.getId());
    	assertEquals("LongName stimmt nicht!","Test Station",station.getLongName());
    	assertEquals("ShortName stimmt nicht!","Test Station",station.getShortName());
    	assertTrue("Es soll eine leere Liste zurückgegeben werden, wenn noch keine Stops zur Station hinzugefügt worden sind!",(new Station("AA","bbb")).getStops().size()==0);
    }
    
    @Test(expected=IllegalArgumentException.class)
	public void testNullConstructor_1(){
		new Station(null,"blub");
		fail("Konstruktor soll bei null-Argumenten entsprechende Exception werfen!");
	}
    
    @Test(expected=IllegalArgumentException.class)
   	public void testNullConstructor_2(){
   		new Station("AB",null);
   		fail("Konstruktor soll bei null-Argumenten entsprechende Exception werfen!");
   	}
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddNullStop() {
    	station.addStop(null);
    	fail("addStop soll eine NullPointerException beim Hinzufügen eines null-Halts werfen!");
    }

    @Test
    public void testAddStop() {
    	station.addStop(stop1);
    	assertTrue("Der Halt muss zur Liste hinzugefügt werden!", station.hasStop(stop1));
    }

    @Test
    public void testRemoveStop() {
	List<Stop> oldStops = new LinkedList<Stop>(station.getStops());

	station.removeStop(null);
	assertTrue("Das Entfernen eines null-Halts darf die Liste nicht verändern!",
		oldStops.equals(station.getStops()));

	Stop stop2 = new Stop(station, StopType.STOP, LocalTime.of(12, 00), LocalTime.of(12, 05), "2",0);
	station.removeStop(stop2);
	assertTrue("Das Entfernen eines in der Liste nicht enthaltenen Halts darf die Liste nicht verändern!",
		oldStops.equals(station.getStops()));
	
	station.removeStop(stop1);
	assertFalse("Der Halt muss gelöscht werden!", station.hasStop(stop1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getStopForTrainRouteNullTest(){
    	station.getStopForTrainRoute(null);
    }
    
    @Test
    public void getStopForTrainRouteNonExistantTest(){
    	Station nowhere = new Station("NWR", "Nowhere");
    	Stop s1 = new Stop(nowhere, StopType.STOP, LocalTime.of(13, 37), LocalTime.of(13, 38), "2", 2);
    	List<Stop> stops = new ArrayList<Stop>();
    	stops.add(s1);
    	TrainCategory category = new TrainCategory("TLX", "TeElIx", "Isn Zuch!", "Damit kann man Dinge von A nach B bewegen. ");
    	TrainRoute route = new TrainRoute("TLX_0815", "3", category, stops, 0);
    	s1.setTrainRoute(route);
    	nowhere.addStop(s1);
    	assertNull("getStopFromTrainRoute should return null if the route is not passing the station. ", station.getStopForTrainRoute(route));
    }
    
    @Test
    public void getStopForTrainRouteTest(){
    	Stop s2 = new Stop(station, StopType.STOP, LocalTime.of(13, 37), LocalTime.of(13, 38), "2", 2);
    	List<Stop> stops = new ArrayList<Stop>();
    	stops.add(s2);
    	TrainCategory category = new TrainCategory("TLX", "TeElIx", "Isn Zuch!", "Damit kann man Dinge von A nach B bewegen. ");
    	TrainRoute route = new TrainRoute("TLX_0815", "3", category, stops, 0);
    	s2.setTrainRoute(route);
    	station.addStop(s2);
    	assertEquals("The stops should be equal! ", station.getStopForTrainRoute(route), s2);
    }
}
