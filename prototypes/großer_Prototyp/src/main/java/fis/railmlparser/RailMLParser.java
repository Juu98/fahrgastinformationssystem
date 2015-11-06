package fis.railmlparser;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.railml.schemas._2009.Railml;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RailMLParser {
    private ApplicationContext appContext;
    private XMLConverter converter;

    public RailMLParser() {
	appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
	converter = (XMLConverter) appContext.getBean("XMLConverter");
	converter.getUnmarshaller().setValidationEventHandler(new CustomValidationEventHandler());
    }

    public Railml parseRailML(String railMLPath) throws IOException, JAXBException {
	return (Railml) converter.convertFromXMLToObject(railMLPath);
    }
}
