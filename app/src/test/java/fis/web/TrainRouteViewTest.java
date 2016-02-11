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
		this.route = new TrainRoute(this.id, "1", this.category, stops, 0);
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
	
	@Test
	public void equalsTest(){
		this.view = new JSONProvider.TrainRouteView(this.route);
		JSONProvider.TrainRouteView new_view = new JSONProvider.TrainRouteView(this.id, this.begin, this.name, this.end);
		assertFalse("Should return false if parameter is null!", this.view.equals(null));
		assertFalse("Should return false if the other object is from another class!", this.view.equals("other class"));
		assertTrue("Should return true if the objects are equal!", this.view.equals(new_view));
	}
}
