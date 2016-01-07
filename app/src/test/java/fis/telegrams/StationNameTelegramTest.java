package fis.telegrams;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class StationNameTelegramTest {
	private StationNameTelegram telegram;
	private byte ID;
	private String code;
	private String name;
	
	@Before
	public void setup(){
		this.ID = (byte) 255;
		this.code = "CODE__";
		this.name = "Name";
	}
	
	@Test
	public void CodeNullConstructorTest(){
		boolean exceptionCatched = false;
		try{
			this.telegram = new StationNameTelegram(this.ID, null, this.name);
		} catch(IllegalArgumentException e) {
			exceptionCatched = true;
		}
		assertTrue("The constructor should throw a IllegalArgumentException if given a null parameter!", exceptionCatched);
	}
	
	@Test
	public void NameNullConstructorTest(){
		boolean exceptionCatched = false;
		try{
			this.telegram = new StationNameTelegram(this.ID, this.code, null);
		} catch(IllegalArgumentException e) {
			exceptionCatched = true;
		}
		assertTrue("The constructor should throw a IllegalArgumentException if given a null parameter!", exceptionCatched);
	}
	
	@Test
	public void GetIDAndConstructorTest(){
		this.telegram = new StationNameTelegram(this.ID, this.code, this.name);
		assertEquals("The getID()-Method didn't return the expected value!", this.ID, this.telegram.getId());
	}
	
	@Test
	public void GetCodeAndConstructorTest(){
		this.telegram = new StationNameTelegram(this.ID, this.code, this.name);
		assertEquals("The getCode()-Method didn't return the expected value!", this.code, this.telegram.getCode());
	}
	
	@Test
	public void GetNameAndConstructorTest(){
		this.telegram = new StationNameTelegram(this.ID, this.code, this.name);
		assertEquals("The getName()-Method didn't return the expected value!", this.name, this.telegram.getName());
	}
}
