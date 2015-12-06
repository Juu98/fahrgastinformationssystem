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
	
	@Test(expected=NullPointerException.class)
	public void testNullConstructor_1(){
		new TrainCategory(null," ", " ", " ");
		fail("Konstruktor soll bei null-Argumenten entsprechende Exception werfen!");	   
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullConstructor_2(){
		new TrainCategory(" ",null, " ", " ");
		fail("Konstruktor soll bei null-Argumenten entsprechende Exception werfen!");	   
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullConstructor_3(){
		new TrainCategory(" "," ", null, " ");
		fail("Konstruktor soll bei null-Argumenten entsprechende Exception werfen!");	   
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullConstructor_4(){
		new TrainCategory(" "," ", " ", null);
		fail("Konstruktor soll bei null-Argumenten entsprechende Exception werfen!");	   
	}
}
