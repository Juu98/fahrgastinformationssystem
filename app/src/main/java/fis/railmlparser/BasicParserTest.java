package fis.railmlparser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.railml.schemas._2009.Railml;
import org.railml.schemas._2009.Timetable;

public class BasicParserTest {
    private RailMLParser parser;
    private Railml railml;
    private Timetable timetable;

    @Before
    public void setUp() throws IOException, JAXBException {
	parser = new RailMLParser();
	railml = parser.parseRailML("EBL Regefahrplan simple.xml");
	timetable = railml.getTimetable();
    }

    @Test
    public void testTimetableIdParsing() {
	assertEquals("tt_EBL_Regefahrplan", timetable.getId());
    }

    @Test
    public void testTimetableNameParsing() {
	assertEquals("EBL Regefahrplan", timetable.getName());
    }
}
