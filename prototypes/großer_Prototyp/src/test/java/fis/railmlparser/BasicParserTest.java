package fis.railmlparser;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.railml.schemas._2009.Infrastructure;
import org.railml.schemas._2009.Railml;
import org.railml.schemas._2009.Timetable;

public class BasicParserTest {
    private RailMLParser parser;
    private Railml railml;
    private Timetable timetable;
    private Infrastructure infrastructure;

    @Before
    public void setUp() throws IOException, JAXBException {
	parser = new RailMLParser();
	railml = parser.parseRailML("EBL Regefahrplan simple.xml");
	timetable = railml.getTimetable();
	infrastructure = railml.getInfrastructure();

    }

    @Test
    public void testTimetableIdParsing() {
	assertNotNull(timetable.getId());
	assertEquals("tt_EBL_Regefahrplan", timetable.getId());
    }

    @Test
    public void testTimetableNameParsing() {
	assertNotNull(timetable.getName());
	assertEquals("EBL Regefahrplan", timetable.getName());
    }

    @Test
    public void testInfrastructureIdParsing() {
	assertNotNull(infrastructure.getId());
	assertEquals("infra_EBL_Regefahrplan", infrastructure.getId());
    }
}
