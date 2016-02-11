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

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

/**
 * XML-Dateien, die von externen Anwendungen generiert wurden, können fehlerhaft
 * und nicht strikt validiert sein und Warnungen beim Konvertieren verursachen.
 * Diese Klasse dient zum Ignorieren solcher Warnungen und Fortführen des
 * Konvertierens.
 *
 * @author Zdravko
 */
public class CustomValidationEventHandler implements ValidationEventHandler {
	/*
	 * (non-Javadoc)
	 *
	 * @see javax.xml.bind.ValidationEventHandler#handleEvent(javax.xml.bind.
	 * ValidationEvent)
	 */
	public boolean handleEvent(ValidationEvent event) {
		return true;
	}

}
