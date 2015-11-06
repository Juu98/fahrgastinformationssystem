package fis.railmlparser;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class CustomValidationEventHandler implements ValidationEventHandler {

    public boolean handleEvent(ValidationEvent event) {
	return true;
    }

}
