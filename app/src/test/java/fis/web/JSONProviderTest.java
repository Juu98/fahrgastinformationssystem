package fis.web;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class JSONProviderTest {
	JSONProvider provider;

	@Before
	public void setup(){
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getStationsNullTest(){
		this.provider.getStations(null);
	}
}
