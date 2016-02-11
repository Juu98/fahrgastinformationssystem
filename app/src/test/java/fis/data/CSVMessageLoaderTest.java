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

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import fis.common.ConfigurationException;

public class CSVMessageLoaderTest {
	private Map<Integer, Message> messages;
	
	@Before
	public void setup(){
		this.messages = new HashMap<Integer, Message>();
		Message m1 = new Message();
		m1.setIndex(1);
		m1.setMessage("Message1");
		Message m2 = new Message();
		m2.setIndex(2);
		m2.setMessage("Message2");
		Message m3 = new Message();
		m3.setIndex(3);
		m3.setMessage("Message 3");
		this.messages.put(m1.getIndex(), m1);
		this.messages.put(m2.getIndex(), m2);
		this.messages.put(m3.getIndex(), m3);
	}
	
	@Test
	public void loadCSVNullTest() throws IllegalArgumentException, FileNotFoundException, ConfigurationException{
		boolean exceptionCatched = false;
		try{
			CSVMessageLoader.loadCSV(null);
		} catch (ConfigurationException e) {
			exceptionCatched = true;
		}
		assertTrue("The constructor should throw a NullPointerException if given a null parameter!", exceptionCatched);
	}
	
	@Test
	public void loadCSVEmptyTest() throws NullPointerException, FileNotFoundException, ConfigurationException{
		boolean exceptionCatched = false;
		try{
			CSVMessageLoader.loadCSV("");
		} catch (ConfigurationException e) {
			exceptionCatched = true;
		}
		assertTrue("The constructor should throw a IllegalArgumentException if given an empty parameter!", exceptionCatched);
	}
	
	@Test
	public void loadCSVMissingFileTest() throws NullPointerException, IllegalArgumentException, ConfigurationException{
		boolean exceptionCatched = false;
		try{
			CSVMessageLoader.loadCSV("/file/not/found.csv");
		} catch (ConfigurationException e) {
			exceptionCatched = true;
		}
		assertTrue("The constructor should throw a FileNotFoundException if given an illegal path!", exceptionCatched);
	}
	
	@Test
	public void loadCSVConfigFalseTest() throws NullPointerException, IllegalArgumentException, FileNotFoundException{
		boolean exceptionCatched = false;
		try{
			CSVMessageLoader.loadCSV("./illegal.csv");
		} catch (ConfigurationException e) {
			exceptionCatched = true;
		}
		assertTrue("The constructor should throw a ConfigurationException if the given file contains multiple messages with the same index!", exceptionCatched);
	}
	
	@Test
	public void loadCSVTest() throws ConfigurationException{
		assertEquals("loadCSV() returned the wrong value!", CSVMessageLoader.loadCSV("./messages.csv").toString(), this.messages.toString());
	}
}
