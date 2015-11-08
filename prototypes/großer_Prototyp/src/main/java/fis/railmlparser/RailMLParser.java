package fis.railmlparser;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.railml.schemas._2009.Railml;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/*
 * Maps RailML data to Java objects.
 */
public class RailMLParser {
    private ApplicationContext appContext;
    private XMLConverter converter;

    // Initialize beans and ignore JAXB2 warnings.
    public RailMLParser() {
	appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
	converter = (XMLConverter) appContext.getBean("XMLConverter");
	converter.getUnmarshaller().setValidationEventHandler(new CustomValidationEventHandler());
    }

    /* 
     * Converts a RailML file and returns a Railml root object which can be used
     * to access all data in the file.
     */
    public Railml parseRailML(String railMLPath) throws IOException, JAXBException {
	return (Railml) converter.convertFromXMLToObject(railMLPath);
    }
}
