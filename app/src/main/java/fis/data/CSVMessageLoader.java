package fis.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import fis.common.ConfigurationException;

/**
 * Eine Ladeklasse für Meldungstexte. 
 * @author kloppstock
 *
 */
public class CSVMessageLoader {
	/**
	 * Statische Lademethode zum Laden von Meldungstexten. 
	 * @param path (Pfad)
	 * @return HashMap<Integer, Message>
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 * @throws ConfigurationException
	 */
	public static Map<Integer, Message> loadCSV(String path) throws NullPointerException, IllegalArgumentException, FileNotFoundException, ConfigurationException {
		if(path == null)
			throw new NullPointerException();
		if(path == "")
			throw new IllegalArgumentException("Path cannot be empty!");
		//Erstellen der nötigen Variablen
		Map<Integer, Message> messages = new HashMap<Integer, Message>();
		CsvToBean<Message> csv = new CsvToBean<Message>();
		//Die folgende Zeile kann eine FileNotFoundException werfen, wenn die Datei nicht gefunden wird. 
		CSVReader reader = new CSVReader(new FileReader(path));
		ColumnPositionMappingStrategy<Message> strategy = new ColumnPositionMappingStrategy<Message>();
		
		//Erstellen einer Tabelle aus Messages mittels der ColumnPisitionMappingStrategy
		strategy.setType(Message.class);
		//Mappen der CSV Variablen zu den Variablen in der Message-Klasse
		String[] columns = new String[]{"index", "message"};
		strategy.setColumnMapping(columns);
		//Einlesen der Variablen in die Erstellte Tabelle und Zusammenfassen der einzelnen Zeilen zu Message-Objekten
		//Anschließend wird eine Liste dieser Elemente erstellt und zurückgegeben. 
		List<Message> list = csv.parse(strategy, reader);
		
		//Konvertieren der Liste in eine HashMap für schnelleren Zugriff auf die Indizes.
		for(Message m : list){
			if(messages.containsKey(m.getIndex()))
				throw new ConfigurationException();
			messages.put(m.getIndex(), m);
		}
		return messages;
	}
}
