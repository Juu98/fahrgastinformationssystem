package fis.data;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MessageTest {
	private Message message;
	
	@Before
	public void setup(){
		this.message = new Message();
	}
	
	@Test
	public void getAndSetIndexTest(){
		this.message.setIndex(3);
		assertEquals("getIndex() returned the wrong value!", this.message.getIndex(), 3);
	}
	
	@Test
	public void setMessageNullTest(){
		boolean exceptionCatched = false;
		try{
			this.message.setMessage(null);
		} catch(IllegalArgumentException e) {
			exceptionCatched = true;
		}
		assertTrue("The constructor should throw a NullPointerException if given a null parameter!", exceptionCatched);
	}
	
	@Test
	public void getAndSetMessageTest(){
		this.message.setMessage("Message");
		assertEquals("getMessage() returned the wrong value!", this.message.getMessage(), "Message");
	}
	
	@Test
	public void toStringTest(){
		this.message.setIndex(2);
		assertEquals("toString() returned the wrong value!", this.message.toString(), "2: ");
		this.message.setMessage("Message");
		assertEquals("toString() returned the wrong value!", this.message.toString(), "2: Message");
	}
}
