package fis.railmlparser;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * Class used by RailMLParser to unmarshal XML data. Exists as a bean.
 * 
 * @author Zdravko
 */
public class XMLConverter {

    private Jaxb2Marshaller unmarshaller;

    /**
     * 
     * @return Current unmarshaller.
     */
    public Jaxb2Marshaller getUnmarshaller() {
	return unmarshaller;
    }

    /**
     * 
     * @param unmarshaller
     *            Unmarshaller to set.
     */
    public void setUnmarshaller(Jaxb2Marshaller unmarshaller) {
	this.unmarshaller = unmarshaller;
    }

    /**
     * 
     * @param xmlfile
     *            XML file to be unmarshalled.
     * @return Root object of tree data structure.
     * @throws IOException
     *             If an input or output exception occurred.
     * 
     * @throws JAXBException
     *             If an internal JAXB Exception occurred.
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
