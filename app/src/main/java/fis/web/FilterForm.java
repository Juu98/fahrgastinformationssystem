package fis.web;

import java.util.List;

/**
 * Interface, das die Nutzerdaten aus dem Filter-Formular abbildet.
 * @author Robert Mörseburg ({@linkplain https://github.com/fl4m})
 */
public interface FilterForm {
	// aktueller Bahnhof
	public String getStationId();
	public String getStationName();
	
	// Zugtypen
	public List<String> getCategories();
	
	// Zielbahnhof
	public String getDestination();
	
	// Zeitfenster
	public String getStart();
	public String getEnd();
	
	// Buttonaktionen
	public String getSubmit();
	public String getReset();
}
