package parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.railml.schemas._2009.Railml;
import org.railml.schemas._2009.Timetable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BasicParserTest {

    ApplicationContext appContext;
    XMLConverter converter;
    Railml railml;
    Timetable timetable;

    @Before
    public void setUp() throws IOException, JAXBException {
	appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
	converter = (XMLConverter) appContext.getBean("XMLConverter");
	converter.getUnmarshaller().setValidationEventHandler(new CustomValidationEventHandler());
	railml = (Railml) converter.convertFromXMLToObject("EBL Regefahrplan simple.xml");
	timetable = railml.getTimetable();
    }

    @Test
    public void testTimetableIdParsing() {
	assertEquals("tt_EBL_Regefahrplan", timetable.getId());
    }

    @Test
    public void testTimetableNameParsing() {
	assertEquals("EBL Regefahrplan", timetable.getName());
    }
}
