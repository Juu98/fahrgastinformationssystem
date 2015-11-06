package parser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

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
	FileInputStream is = null;
	try {
	    is = new FileInputStream(xmlfile);
	    return getUnmarshaller().unmarshal(new StreamSource(is));
	} finally {
	    if (is != null) {
		is.close();
	    }
	}
    }
}
