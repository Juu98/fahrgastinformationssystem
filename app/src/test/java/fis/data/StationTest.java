package fis.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

    @Test(expected = NullPointerException.class)
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
	assertTrue("Das Entfernen eines null-Halts muss die Liste nicht verändern!",
		oldStops.equals(station.getStops()));

	Stop stop2 = new Stop(station, StopType.STOP, LocalTime.of(12, 00), LocalTime.of(12, 05), "2");
	station.removeStop(stop2);
	assertTrue("Das Entfernen eines in der Liste nicht enthaltenen Halts muss die Liste nicht verändern!",
		oldStops.equals(station.getStops()));
	
	station.removeStop(stop1);
	assertFalse("Der Halt muss gelöscht werden!", station.hasStop(stop1));
    }

}
