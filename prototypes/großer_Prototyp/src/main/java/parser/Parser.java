package parser;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;

import org.railml.schemas._2009.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Parser {
    @SuppressWarnings("unused")
    public static void main(String[] args) throws IOException, JAXBException, UnmarshalException {
	@SuppressWarnings("resource")
	ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
	XMLConverter converter = (XMLConverter) appContext.getBean("XMLConverter");
	converter.getUnmarshaller().setValidationEventHandler(new CustomValidationEventHandler());
	Railml railml = (Railml) converter.convertFromXMLToObject("EBL Regefahrplan simple.xml");
	System.out.println(railml.getTimetable().getId());

    }
}
