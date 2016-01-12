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
