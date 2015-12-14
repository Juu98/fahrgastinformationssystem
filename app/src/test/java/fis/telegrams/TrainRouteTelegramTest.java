package fis.telegrams;

import fis.data.StopType;
import fis.data.Station;
import fis.data.Stop;
import fis.data.StopType;
import fis.data.TrainCategory;
import fis.data.TrainRoute;

import java.time.LocalTime;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TrainRouteTelegramTest {
	private TrainRouteTelegram telegram;
	private TrainRoute route1;
	private TrainRoute route2;
	private List<Stop> stops1;
	private List<Stop> stops2;
	private List<Stop> stops3;
	
	@Before
	public void setup(){
		TrainCategory category = new TrainCategory("TC_ID", "RE", "Regional Express. ", "Menschen fahren da drin. ");
		Station dresden = new Station("DDHBF", "Dresden Hauptbahnhof");
		Station dresden_neustadt = new Station("DDNEU", "Dresden Neustadt");
		Station tharandt = new Station("THRNDT", "Tharandt");
		Station freiberg = new Station("FB", "Freiberg");
		Station klingenberg = new Station("KLICO", "Klingenberg-Colmnitz");
		Station chemnitz = new Station("CHHBF", "Chemnitz Hauptbahnhof");
		this.stops1 = new ArrayList<Stop>();
		this.stops1.add(new Stop(dresden_neustadt, StopType.BEGIN, LocalTime.of(13, 37), LocalTime.of(13, 38), "3",0));
		this.stops1.add(new Stop(dresden, StopType.PASS, LocalTime.of(14, 37), LocalTime.of(14, 38), "3",0));
		this.stops1.add(new Stop(tharandt, StopType.PASS, LocalTime.of(15, 37), LocalTime.of(15, 38), "3",0));
		this.stops2 = new ArrayList<Stop>();
		this.stops2.add(new Stop(klingenberg, StopType.PASS, LocalTime.of(16, 37), LocalTime.of(16, 38), "3",0));
		this.stops2.add(new Stop(freiberg, StopType.PASS, LocalTime.of(17, 37), LocalTime.of(17, 38), "3",0));
		this.stops2.add(new Stop(chemnitz, StopType.END, LocalTime.of(18, 37), LocalTime.of(18, 38), "3",0));
		this.stops3 = new ArrayList<Stop>();
		this.stops3.addAll(stops1);
		this.stops3.addAll(stops2);
		this.route1 = new TrainRoute("ID_1", 9000, category, stops1);
		this.route2 = new TrainRoute("ID_2", 9001, category, stops2);
	}
	
	@Test
	public void NullConstructorTest(){
		boolean exceptionCatched = false;
		try{
			this.telegram = new TrainRouteTelegram((TrainRoute)null);
		} catch(NullPointerException e) {
			exceptionCatched = true;
		}
		assertTrue("The constructor should throw a NullPointerException if given a null parameter!", exceptionCatched);
	}
	
	@Test
	public void appendRouteNullTest(){
		boolean exceptionCatched = false;
		this.telegram = new TrainRouteTelegram(this.route1);
		try{
			telegram.appendRoute(null);
		} catch(NullPointerException e) {
			exceptionCatched = true;
		}
		assertTrue("appedRoute() should throw a NullPointerException if given a null parameter!", exceptionCatched);
	}
	
	@Test
	public void appendRouteTest(){
		this.telegram = new TrainRouteTelegram(this.route1);
		this.telegram.appendRoute(this.route2);
		assertEquals("The appendRoute()-Method didn't return the expected value!", this.stops3, this.telegram.getTrainRoute().getStops());
	}
	
	@Test
	public void GetTrainRouteAndConstructorTest(){
		this.telegram = new TrainRouteTelegram(this.route1);
		assertEquals("The getTrainRoute()-Method didn't return the expected value!", this.route1, this.telegram.getTrainRoute());
	}
}
