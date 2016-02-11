/* Copyright 2016 Erik Schölzel, Robert Mörseburg, Zdravko Yanakiev, Jonas Schenke, Oliver Schmidt
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
		} catch(IllegalArgumentException e) {
			exceptionCatched = true;
		}
		assertTrue("The constructor should throw a IllegalArgumentException if given a null parameter!", exceptionCatched);
	}
	
	@Test
	public void ConstructorAndGetterTest(){
		this.telegram = new LabTimeTelegram(this.time);
		assertEquals("The getTime()-Method didn't return the expected value!", this.time, this.telegram.getTime());
	}
}
