package fis.railmlparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.railml.schemas._2009.Infrastructure;
import org.railml.schemas._2009.Railml;
import org.railml.schemas._2009.Timetable;

public class FullTimetableParsingTest {
    private RailMLParser parser;
    private Railml railml;
    private Timetable timetable;
    private Infrastructure infrastructure;

    @Before
    public void setUp() {
	parser = new RailMLParser();
    }

    @Test
    public void testRailMLParsing() {
	try {
	    railml = parser.parseRailML("2015-04-27_EBL-Regefahrplan-Export.xml");
	    timetable = railml.getTimetable();
	    infrastructure = railml.getInfrastructure();
	} catch (NullPointerException e) {
	    fail("Parsed RailML should not be null!");
	    e.printStackTrace();
	} catch (IOException e) {
	    fail();
	    e.printStackTrace();
	} catch (JAXBException e) {
	    fail();
	    e.printStackTrace();
	}
    }

}
