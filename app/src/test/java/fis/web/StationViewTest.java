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
		this.view = new StationView("", "");	//TODO: hier weiter machen
	}
	
	@Test
	public void getIDAndConstructorTest(){
		
	}
}
