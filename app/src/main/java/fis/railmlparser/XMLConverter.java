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

import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;

/**
 * Klasse, die von RailMLParser benutzt wird, um XML-Daten zu konvertieren.
 * Existiert als Bean.
 *
 * @author Zdravko
 */
public class XMLConverter {

	private Jaxb2Marshaller unmarshaller;

	/**
	 * @return Der aktuelle Unmarshaller.
	 */
	public Jaxb2Marshaller getUnmarshaller() {
		return unmarshaller;
	}

	/**
	 * @param unmarshaller Setzt Unmarshaller.
	 */
	public void setUnmarshaller(Jaxb2Marshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}

	/**
	 * @param xmlfile XML-Datei, die konvertiert werden soll.
	 * @return Wurzelobjekt der Baumdatenstruktur.
	 * @throws IOException   Bei einer Ein- oder Ausgabeausnahme.
	 * @throws JAXBException Bei einer inneren JAXB-Ausnahme.
	 */
	public Object convertFromXMLToObject(String xmlfile) throws IOException, JAXBException {
		InputStream is = null;
		try {
			is = new ClassPathResource(xmlfile).getInputStream();
			return getUnmarshaller().unmarshal(new StreamSource(is));
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
}
