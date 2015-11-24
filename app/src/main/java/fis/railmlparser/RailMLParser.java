package fis.railmlparser;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.railml.schemas._2009.Railml;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Main RailML parser class. Maps RailML data to Java objects.
 * 
 * @author Zdravko
 */
public class RailMLParser {
    private ApplicationContext appContext;
    private XMLConverter converter;

    /**
     * Sole constructor, initializes beans and sets a custom
     * ValidationEventHandler in order to ignore JAXB2 warnings.
     */
    public RailMLParser() {
	appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
	converter = (XMLConverter) appContext.getBean("XMLConverter");
	converter.getUnmarshaller().setValidationEventHandler(new CustomValidationEventHandler());
    }

    /**
     * Converts a RailML file into an object tree and returns a Railml root
     * object which can be used to access all data in the generated tree
     * structure.
     * 
     * @param railMLpath
     *            Path of RailML file in the classpath.
     * 
     * @return Railml root object.
     * 
     * @throws IOException
     *             If an input or output exception occurred.
     * @throws JAXBException
     *             If an internal JAXB Exception occurred.
     */
    public Railml parseRailML(String railMLPath) throws IOException, JAXBException {
	return (Railml) converter.convertFromXMLToObject(railMLPath);
    }
}
