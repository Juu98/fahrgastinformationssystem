package fis.railmlparser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.core.io.ClassPathResource;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/* 
 * Class used by RailMLParser to unmarshal XML data. Exists as a bean.
 */
public class XMLConverter {

    private Jaxb2Marshaller marshaller;
    private Jaxb2Marshaller unmarshaller;

    public Jaxb2Marshaller getMarshaller() {
	return marshaller;
    }

    public void setMarshaller(Jaxb2Marshaller marshaller) {
	this.marshaller = marshaller;
    }

    public Jaxb2Marshaller getUnmarshaller() {
	return unmarshaller;
    }

    public void setUnmarshaller(Jaxb2Marshaller unmarshaller) {
	this.unmarshaller = unmarshaller;
    }

    public void convertFromObjectToXML(Object object, String filepath) throws IOException, JAXBException {
	FileOutputStream os = null;
	try {
	    os = new FileOutputStream(filepath);
	    getMarshaller().marshal(object, new StreamResult(os));
	} finally {
	    if (os != null) {
		os.close();
	    }
	}
    }

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
