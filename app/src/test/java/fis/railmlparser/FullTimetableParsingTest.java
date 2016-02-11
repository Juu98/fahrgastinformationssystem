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
