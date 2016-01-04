package fis.web;

import org.junit.Test;

import fis.data.Station;
import fis.web.JSONProvider.StationView;

import org.junit.Before;
import static org.junit.Assert.*;

public class StationViewTest {
	private JSONProvider provider;
	private JSONProvider.StationView view;
	private String id; 
	private String name;
	private Station station;
	
	@Before()
	public void setup(){
		this.id = "ID";
		this.name = "Name";
		this.station = new Station(this.id, this.name);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void Constructor1NullTest(){
		this.view = new StationView("not_null", null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void Constructor2NullTest(){
		this.view = new StationView(null, "not_null");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void Constructor3NullTest(){
		this.view = new StationView(null);
	}
	
	@Test
	public void getIDAndConstructor1Test(){
		this.view = new StationView(this.id, this.name);
		assertEquals(this.view.getId(), this.id);
		assertEquals(this.view.getName(), this.name);
	}
	
	@Test
	public void getIDAndConstructor2Test(){
		this.view = new StationView(this.station);
		assertEquals(this.view.getId(), this.id);
		assertEquals(this.view.getName(), this.name);
	}
	
	@Test
	public void equalsTest(){
		this.view = new JSONProvider.StationView(station);
		JSONProvider.StationView new_view = new JSONProvider.StationView("ID", "Name");
		assertFalse("Should return false if parameter is null!", this.view.equals(null));
		assertFalse("Should return false if the other object is from another class!", this.view.equals("other class"));
		assertTrue("Should return true if the objects are equal!", this.view.equals(new_view));
	}
}
