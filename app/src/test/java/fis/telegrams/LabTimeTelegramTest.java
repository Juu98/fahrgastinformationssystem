package fis.telegrams;

import java.time.LocalTime;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LabTimeTelegramTest {
	private LabTimeTelegram telegram;
	private LocalTime time;
	
	@Before
	public void setup(){
		this.time = LocalTime.of(13, 37);
	}
	
	@Test
	public void NullConstructorTest(){
		boolean exceptionCatched = false;
		try{
			this.telegram = new LabTimeTelegram(null);
		} catch(NullPointerException e) {
			exceptionCatched = true;
		}
		assertTrue("The constructor should throw a NullPointerException if given a null parameter!", exceptionCatched);
	}
	
	@Test
	public void ConstructorAndGetterTest(){
		this.telegram = new LabTimeTelegram(this.time);
		assertEquals("The getTime()-Method didn't return the expected value!", this.time, this.telegram.getTime());
	}
}
