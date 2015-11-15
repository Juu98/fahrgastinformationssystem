package fis.railmlparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.railml.schemas._2009.ECategory;
import org.railml.schemas._2009.Infrastructure;
import org.railml.schemas._2009.Railml;
import org.railml.schemas._2009.Timetable;

public class FullTimetableParsingTest {
    private RailMLParser parser;
    private Railml railml;
    private Timetable timetable;
    private Infrastructure infrastructure;

    @Before
    public void setUp() throws IOException, JAXBException {
	parser = new RailMLParser();
	railml = parser.parseRailML("2015-04-27_EBL-Regefahrplan-Export.xml");
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

    @Test
    public void testCategoriesParsing() {
	List<ECategory> categories = timetable.getCategories().getCategory();
	String[] categoryIds = new String[] { "cat_DBM", "cat_RB", "cat_IC", "cat_RE", "cat_KT", "cat_EK", "cat_DGS",
		"cat_Sperrf", "cat_Thalys", "cat_ICE" };
	assertEquals(10, categories.size());
	for (int i = 0; i <= 9; i++) {
	    assertEquals(categoryIds[i], categories.get(i).getId());
	}
    }

}
