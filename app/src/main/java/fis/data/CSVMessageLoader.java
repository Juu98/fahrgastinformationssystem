package fis.data;

import java.io.FileReader;
import java.util.*;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

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
	 * @throws Exception
	 */
	public static Map<Integer, Message> loadCSV(String path) throws NullPointerException, Exception {
		//TODO: rausfinden, welche exceptions geworfen werden können
		if(path == null)
			throw new NullPointerException();
		//Erstellen der nötigen Variablen
		Map<Integer, Message> messages = new HashMap<Integer, Message>();
		CsvToBean<Message> csv = new CsvToBean<Message>();
		CSVReader reader = new CSVReader(new FileReader(path));
		ColumnPositionMappingStrategy<Message> strategy = new ColumnPositionMappingStrategy<Message>();
		
		//TODO: rausfinden, was hier passiert
		strategy.setType(Message.class);
		String[] columns = new String[]{"Index", "Message"};
		strategy.setColumnMapping(columns);
		List<Message> list = csv.parse(strategy, reader);
		
		//Konvertieren der Liste in eine HashMap für schnelleren Zugriff auf die Indizes.
		for(Object o : list){
			Message m = (Message) o;
			messages.put(m.getIndex(), m);
		}
		return messages;
	}
}
