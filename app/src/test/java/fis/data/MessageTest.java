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
