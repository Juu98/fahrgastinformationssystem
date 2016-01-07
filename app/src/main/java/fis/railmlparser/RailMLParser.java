package fis.railmlparser;

import org.railml.schemas._2009.Railml;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Hauptklasse des RailML-Parsers. Erstellt Javaobjekte aus RailML-Daten.
 * 
 * @author Zdravko
 */
public class RailMLParser {
    private ApplicationContext appContext;
    private XMLConverter converter;

    /**
     * Einziger Konstruktor, initialisiert die Beans und setzt einen speziellen
     * ValidationEventHandler, um JAXB2-Warnungen zu ignorieren.
     * 
     * @see CustomValidationEventHandler
     */
    public RailMLParser() {
	appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
	converter = (XMLConverter) appContext.getBean("XMLConverter");
	converter.getUnmarshaller().setValidationEventHandler(new CustomValidationEventHandler());
    }

    /**
     * Konvertiert eine RailML-Datei in einen Objektbaum und gibt ein
     * Railml-Wurzelobjekt, das zum Abrufen der Daten in der generierten
     * Baumstruktur verwendet werden kann, zurück.
     * 
     * 
     * @param railMLpath
     *            Pfad der RailML-Datei im Klassenpfad.
     * 
     * @return Railml-Wurzelobjekt
     * 
     * @throws IOException
     *             Bei einer Ein- oder Ausgabeausnahme.
     * @throws JAXBException
     *             Bei einer inneren JAXB-Ausnahme.
     */
    public Railml parseRailML(String railMLPath) throws IOException, JAXBException {
	return (Railml) converter.convertFromXMLToObject(railMLPath);
    }
}
