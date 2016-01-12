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
		m3.setMessage("Message3");
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
		} catch (IllegalArgumentException e) {
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
	public void loadCSVTest() throws NullPointerException, IllegalArgumentException, FileNotFoundException, ConfigurationException{
		assertEquals("loadCSV() returned the wrong value!", CSVMessageLoader.loadCSV("./messages.csv").toString(), this.messages.toString());
	}
}
