package fis;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

public class RailML2DataTest {

    @Before
    public void setUp() {
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadML() throws Exception {
	RailML2Data.loadML("ungültiger_Pfad.xml");
	fail("Das Laden eines ungültigen Pfads soll einen Fehler werfen!");
    }
}
