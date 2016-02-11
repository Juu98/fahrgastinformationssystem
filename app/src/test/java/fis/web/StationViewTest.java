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
		this.view = new StationView("not_null", null, -1, -1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void Constructor2NullTest(){
		this.view = new StationView(null, "not_null", -1, -1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void Constructor3NullTest(){
		this.view = new StationView(null);
	}
	
	@Test
	public void getIDAndConstructor1Test(){
		this.view = new StationView(this.id, this.name, -1, -1);
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
		JSONProvider.StationView new_view = new JSONProvider.StationView("ID", "Name", -1, -1);
		assertFalse("Should return false if parameter is null!", this.view.equals(null));
		assertFalse("Should return false if the other object is from another class!", this.view.equals("other class"));
		assertTrue("Should return true if the objects are equal!", this.view.equals(new_view));
	}
}
