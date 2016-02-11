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

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class StationNameTelegramTest {
	private StationNameTelegram telegram;
	private byte ID;
	private String code;
	private String name;
	private float x;
	private float y;
	
	@Before
	public void setup(){
		this.ID = (byte) 255;
		this.code = "CODE__";
		this.name = "Name";
		this.x = -1f;
		this.y = -1f;
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void CodeNullConstructorTest(){
		this.telegram = new StationNameTelegram(this.ID, null, this.name, this.x, this.y);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void NameNullConstructorTest(){
		this.telegram = new StationNameTelegram(this.ID, this.code, null, this.x, this.y);
	}
	
	@Test
	public void GetIDAndConstructorTest(){
		this.telegram = new StationNameTelegram(this.ID, this.code, this.name, this.x, this.y);
		assertEquals("The getID()-Method didn't return the expected value!", this.ID, this.telegram.getId());
	}
	
	@Test
	public void GetCodeAndConstructorTest(){
		this.telegram = new StationNameTelegram(this.ID, this.code, this.name, this.x, this.y);
		assertEquals("The getCode()-Method didn't return the expected value!", this.code, this.telegram.getCode());
	}
	
	@Test
	public void GetNameAndConstructorTest(){
		this.telegram = new StationNameTelegram(this.ID, this.code, this.name, this.x, this.y);
		assertEquals("The getName()-Method didn't return the expected value!", this.name, this.telegram.getName());
	}
}
