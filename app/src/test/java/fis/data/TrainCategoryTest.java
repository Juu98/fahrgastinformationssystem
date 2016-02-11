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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class TrainCategoryTest {
	TrainCategory cat1;
	
	
	@Before
	public void setUp(){
		cat1=new TrainCategory("1","eins","desc1","trainUsage1");
		
	}
	
	@Test
	public void testGetter(){
		assertEquals("Id stimmt nicht überein!",cat1.getId(),"1");
		assertEquals("Name stimmt nicht überein!",cat1.getName(),"eins");
		assertEquals("Description stimmt nicht überein!", cat1.getDescription(),"desc1");
		assertEquals("TrainUsage stimmt nicht überein!", cat1.getTrainUsage(),"trainUsage1");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullConstructor_1(){
		new TrainCategory(null," ", " ", " ");
		fail("Konstruktor soll bei null-Argumenten entsprechende Exception werfen!");	   
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullConstructor_2(){
		new TrainCategory(" ",null, " ", " ");
		fail("Konstruktor soll bei null-Argumenten entsprechende Exception werfen!");	   
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullConstructor_3(){
		new TrainCategory(" "," ", null, " ");
		fail("Konstruktor soll bei null-Argumenten entsprechende Exception werfen!");	   
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullConstructor_4(){
		new TrainCategory(" "," ", " ", null);
		fail("Konstruktor soll bei null-Argumenten entsprechende Exception werfen!");	   
	}
}
