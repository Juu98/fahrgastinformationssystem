package fis;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

public class RailML2DataTest {
    private RailML2Data railML2Data;

    @Before
    public void setUp() {
	this.railML2Data = new RailML2Data();
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadML() throws Exception {
	railML2Data.loadML("ungültiger_Pfad.xml");
	fail("Das Laden eines ungültigen Pfads soll einen Fehler werfen!");
    }
}
