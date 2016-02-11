/* Copyright 2016 Erik Schölzel, Robert Mörseburg, Zdravko Yanakiev, Jonas Schenke, Oliver Schmidt
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

import org.railml.schemas._2009.Railml;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Hauptklasse des RailML-Parsers. Erstellt Javaobjekte aus RailML-Daten.
 *
 * @author Zdravko
 */
public class RailMLParser {
	private ApplicationContext appContext;
	private XMLConverter converter;

	/**
	 * Einziger Konstruktor, initialisiert die Beans und setzt einen speziellen
	 * ValidationEventHandler, um JAXB2-Warnungen zu ignorieren.
	 *
	 * @see CustomValidationEventHandler
	 */
	public RailMLParser() {
		appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		converter = (XMLConverter) appContext.getBean("XMLConverter");
		converter.getUnmarshaller().setValidationEventHandler(new CustomValidationEventHandler());
	}

	/**
	 * Konvertiert eine RailML-Datei in einen Objektbaum und gibt ein
	 * Railml-Wurzelobjekt, das zum Abrufen der Daten in der generierten
	 * Baumstruktur verwendet werden kann, zurück.
	 *
	 * @param railMLPath Pfad der RailML-Datei im Klassenpfad.
	 * @return Railml-Wurzelobjekt
	 * @throws IOException   Bei einer Ein- oder Ausgabeausnahme.
	 * @throws JAXBException Bei einer inneren JAXB-Ausnahme.
	 */
	public Railml parseRailML(String railMLPath) throws IOException, JAXBException {
		return (Railml) converter.convertFromXMLToObject(railMLPath);
	}
}
