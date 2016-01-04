package fis.web;

import org.junit.Test;

import fis.data.Station;
import fis.data.TrainCategory;
import fis.data.TrainRoute;
import fis.web.JSONProvider.TrainRouteView;
import fis.data.Stop;
import fis.data.StopType;

import org.junit.Before;
import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TrainRouteViewTest {
	private String id;
	private String name;
	private Station beginStation;
	private Station endStation;
	private TrainCategory category;
	private List<Stop> stops;
	private Stop first;
	private Stop last;
	private TrainRoute route;
	private JSONProvider.StationView begin;
	private JSONProvider.StationView end;
	private JSONProvider.TrainRouteView view;
	
	@Before
	public void setup(){
		this.id = "ID";
		this.beginStation = new Station("BEG", "Begin");
		this.endStation = new Station("END", "End");
		this.category = new TrainCategory("CAT_ID", "Categoryname", "CategoryDescription", "Just get in. ");
		this.first = new Stop(beginStation, StopType.BEGIN, LocalTime.of(13, 37), LocalTime.of(13, 37), "8", 404);
		this.last = new Stop(endStation, StopType.END, LocalTime.of(13, 39), LocalTime.of(13, 40), "8", 404);
		this.stops = new ArrayList<Stop>();
		this.stops.add(first);
		this.stops.add(last);
		this.route = new TrainRoute(this.id, 1, this.category, stops);
		this.first.setTrainRoute(route);
		this.last.setTrainRoute(route);
		this.name = this.route.toString();
		this.begin = new JSONProvider.StationView(this.beginStation);
		this.end = new JSONProvider.StationView(this.endStation);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void ConstructorIDNullTest(){
		this.view = new TrainRouteView(null, this.begin, this.name, this.end);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void ConstructorBeginNullTest(){
		this.view = new TrainRouteView(this.id, null, this.name, this.end);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void ConstructorNameNullTest(){
		this.view = new TrainRouteView(this.id, this.begin, null, this.end);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void ConstructorEndNullTest(){
		this.view = new TrainRouteView(this.id, this.begin, this.name, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void ConstructorRouteNullTest(){
		this.view = new TrainRouteView(null);
	}
	
	@Test
	public void Constructor1AndGetterTest(){
		this.view = new TrainRouteView(this.id, this.begin, this.name, this.end);
		assertEquals(this.view.getBegin(), this.begin);
		assertEquals(this.view.getEnd(), this.end);
		assertEquals(this.view.getId(), this.id);
		assertEquals(this.view.getName(), this.name);
	}
	
	@Test
	public void Constructor2AndGetterTest(){
		this.view = new TrainRouteView(this.route);
		assertEquals(this.view.getBegin(), this.begin);
		assertEquals(this.view.getEnd(), this.end);
		assertEquals(this.view.getId(), this.id);
		assertEquals(this.view.getName(), this.name);
	}
}
