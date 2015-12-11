package fis.data;

import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class StationTest {
    private Station station;
    private Stop stop1;

    @Before
    public void setUp() throws Exception {
	this.station = new Station("TS", "Test Station");
	this.stop1 = new Stop(station, StopType.STOP, LocalTime.of(13, 37), LocalTime.of(16, 20), "1");
    }

    @Test
    public void testGetter(){
    	assertEquals("ID stimmt nicht!","TS",station.getId());
    	assertEquals("Name stimmt nicht!","Test Station",station.getName());
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

	Stop stop2 = new Stop(station, StopType.STOP, LocalTime.of(12, 00), LocalTime.of(12, 05), "2");
	station.removeStop(stop2);
	assertTrue("Das Entfernen eines in der Liste nicht enthaltenen Halts darf die Liste nicht verändern!",
		oldStops.equals(station.getStops()));
	
	station.removeStop(stop1);
	assertFalse("Der Halt muss gelöscht werden!", station.hasStop(stop1));
    }

}
