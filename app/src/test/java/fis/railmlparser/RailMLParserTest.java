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
