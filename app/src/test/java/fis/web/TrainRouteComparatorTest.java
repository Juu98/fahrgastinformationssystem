/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fis.web;

import fis.FilterTime;
import fis.FilterType;
import fis.data.Station;
import fis.data.Stop;
import fis.data.StopType;
import fis.data.TrainCategory;
import fis.data.TrainRoute;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link fis.web.TrainRouteComparator}.
 * @author Robert
 */
public class TrainRouteComparatorTest {
	private final Station sA = new Station("sA", "Station A");
	private final Station sB = new Station("sB", "Station B");
	private final Station sC = new Station("sC", "Station C");
	private final TrainCategory tc = new TrainCategory("DUMMY", "Dummy Category", "A useless dummy train category.", "TESTING");
	
	private TrainRoute tr1, tr2, tr3;
	private List<TrainRoute> trList = new ArrayList<>();
	private TrainRouteComparator trc;
	
	@Before
	public void setUp(){
		// TR1: A -> B -> C
		tr1 = new TrainRoute("tr1", 1, this.tc, new ArrayList<Stop>());
		tr1.getStops().add(new Stop(sA, StopType.BEGIN, null, LocalTime.of(12, 00), null));
		tr1.getStops().add(new Stop(sB, StopType.STOP, LocalTime.of(12, 15), LocalTime.of(12, 20), null));
		tr1.getStops().add(new Stop(sC, StopType.END, LocalTime.of(12, 30), null, null));
		this.trList.add(tr1);
		
		// TR2: C -> B -> A
		tr2 = new TrainRoute("tr2", 2, this.tc, new ArrayList<Stop>());
		tr2.getStops().add(new Stop(sC, StopType.BEGIN, null, LocalTime.of(12, 00), null));
		tr2.getStops().add(new Stop(sB, StopType.STOP, LocalTime.of(12, 17), LocalTime.of(12, 18), null));
		tr2.getStops().add(new Stop(sA, StopType.END, LocalTime.of(12, 30), null, null));
		this.trList.add(tr2);
		
		// TR3: A -> C
		tr3 = new TrainRoute("tr3", 3, this.tc, new ArrayList<Stop>());
		tr3.getStops().add(new Stop(sA, StopType.BEGIN, null, LocalTime.of(12, 00), null));
		tr3.getStops().add(new Stop(sC, StopType.END, LocalTime.of(12, 30), null, null));
	}
	
	@Test
	public void orderScheduledDeparture(){
		trc = new TrainRouteComparator(sB, FilterTime.SCHEDULED, FilterType.DEPARTURE);
		List<TrainRoute> expected = Arrays.asList(this.tr2, this.tr1);
		trList.sort(trc);		
		
		Assert.assertEquals(trList, expected);
	}
	
	@Test
	public void orderScheduledArrival(){
		trc = new TrainRouteComparator(sB, FilterTime.SCHEDULED, FilterType.ARRIVAL);
		List<TrainRoute> expected = Arrays.asList(this.tr1, this.tr2);
		trList.sort(trc);
		
		Assert.assertEquals(trList, expected);
	}
	 
	@Test(expected = IllegalArgumentException.class)
	public void handlesNull(){
		trList.add(this.tr3);
		trc = new TrainRouteComparator(sB, FilterTime.SCHEDULED, FilterType.ARRIVAL);
		trList.sort(trc);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void handlesAny(){
		trc = new TrainRouteComparator(sA, FilterTime.SCHEDULED, FilterType.ANY);
	}
}
