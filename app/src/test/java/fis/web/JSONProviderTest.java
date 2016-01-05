package fis.web;

import org.junit.Test;

import fis.data.Station;
import fis.data.Stop;
import fis.data.StopType;
import fis.data.TrainCategory;
import fis.data.TrainRoute;
import fis.web.JSONProvider.StationView;

import org.junit.Before;
import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class JSONProviderTest {
	private List<Station> emptyStationList;
	private List<Station> stationList;
	private List<TrainRoute> emptyTrainRouteList;
	private List<TrainRoute> trainRoutesList;
	private Station dresden;
	private Station freiberg;
	private Station tharandt;
	private Station hamburg;
	private TrainCategory category;
	private Stop stop1;
	private Stop stop2;
	private Stop stop3;
	private Stop stop4;
	private List<Stop> stops1;
	private List<Stop> stops2;
	private TrainRoute route1;
	private TrainRoute route2;
	private List<JSONProvider.StationView> expectedStationList;
	private List<JSONProvider.TrainRouteView> expectedTrainRouteView;
	private JSONProvider provider;

	@Before
	public void setup(){
		//Erstelle Stationen
		this.freiberg = new Station("FG", "Freiberg");
		this.dresden = new Station("DD", "Dresden");
		this.tharandt = new Station("THD", "Tharandt");
		this.hamburg = new Station("HH", "Hamburg");
		//Erstellen der Stationslisten
		this.emptyStationList = new ArrayList<Station>();
		this.stationList = new ArrayList<Station>();
		this.stationList.add(dresden);
		this.stationList.add(freiberg);
		//Erstellen der ersten Zuglaufes
		this.category = new TrainCategory("ID", "Name", "Beschreibung", "Auf auf und davon!");
		this.stop1 = new Stop(dresden, StopType.BEGIN, LocalTime.of(13, 37), LocalTime.of(13, 38), "3", 404);
		this.stop2 = new Stop(freiberg, StopType.END, LocalTime.of(13, 39), LocalTime.of(13, 40), "2", 404);
		this.stops1 = new ArrayList<Stop>();
		this.stops1.add(stop1);
		this.stops1.add(stop2);
		this.route1 = new TrainRoute("ID1", 3, category, stops1);
		this.stop1.setTrainRoute(route1);
		this.stop2.setTrainRoute(route1);
		//Erstellen des 2. Zuglaufes
		this.stop3 = new Stop(tharandt, StopType.BEGIN, LocalTime.of(13, 37), LocalTime.of(13, 38), "3", 404);
		this.stop4 = new Stop(hamburg, StopType.END, LocalTime.of(15, 37), LocalTime.of(15, 38), "2", 404);
		this.stops2 = new ArrayList<Stop>();
		this.stops2.add(stop3);
		this.stops2.add(stop4);
		this.route2 = new TrainRoute("ID2", 25, category, stops2);
		stop3.setTrainRoute(route2);
		stop4.setTrainRoute(route2);
		//Erstellen und Füllen der Zuglauflisten
		this.emptyTrainRouteList = new ArrayList<TrainRoute>();
		this.trainRoutesList = new ArrayList<TrainRoute>();
		this.trainRoutesList.add(route1);
		this.trainRoutesList.add(route2);
		//Erstelle Musterlösungen für die beiden Funktionen
		this.expectedStationList = new ArrayList<JSONProvider.StationView>();
		this.expectedStationList.add(new JSONProvider.StationView(dresden));
		this.expectedStationList.add(new JSONProvider.StationView(freiberg));
		this.expectedTrainRouteView = new ArrayList<JSONProvider.TrainRouteView>();
		this.expectedTrainRouteView.add(new JSONProvider.TrainRouteView(route1));
		this.expectedTrainRouteView.add(new JSONProvider.TrainRouteView(route2));
		this.provider = new JSONProvider();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getStationsNullTest(){
		this.provider.getStations(null);
	}
	
	@Test
	public void getStationsEmptyTest(){
		List<JSONProvider.StationView> emptyList = new ArrayList<StationView>();
		assertArrayEquals(this.provider.getStations(emptyStationList).toArray(), emptyList.toArray());
	}
	
	@Test
	public void getStationTest(){
		assertArrayEquals(this.provider.getStations(stationList).toArray(), this.expectedStationList.toArray());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getTrainRoutesNullTest(){
		this.provider.getTrainRoutes(null);
	}
	
	@Test
	public void getTrainRoutesEmptyTest(){
		List<JSONProvider.TrainRouteView> emptyList = new ArrayList<JSONProvider.TrainRouteView>();
		assertArrayEquals(this.provider.getTrainRoutes(emptyTrainRouteList).toArray(), emptyList.toArray());
	}
	
	@Test
	public void getTrainRouteTest(){
		assertArrayEquals(this.provider.getTrainRoutes(trainRoutesList).toArray(), this.expectedTrainRouteView.toArray());
	}
}
