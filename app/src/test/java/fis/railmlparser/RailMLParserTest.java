package fis.railmlparser;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.railml.schemas._2009.Railml;

public class RailMLParserTest {
    private RailMLParser railMLParser;

    @Before
    public void setUp() throws Exception {
	this.railMLParser = new RailMLParser();
    }

    @Test(expected = FileNotFoundException.class)
    public void testParseRailMLInvalidPath() throws IOException, JAXBException {
	railMLParser.parseRailML("ungültiger_Pfad.xml");
	fail("Das Laden eines ungültigen Pfads soll einen Fehler werfen!");
    }

    @Test
    public void testParseRailML() throws IOException, JAXBException {
	Railml railml = railMLParser.parseRailML("EBL Regelfahrplan simple.xml");
	assertTrue("Das Parsen einer gültigen Datei soll ein Railml-Objekt erzeugen!",
		railml.getClass().equals(Railml.class));
    }
}
