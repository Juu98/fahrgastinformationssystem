package fis.data;

import java.util.LinkedList;
import java.util.List;

/**
 * Bahnhofsklasse.
 * 
 * @author Luux
 *
 */
public class Station {
	
    private String longName;
    private String shortName;
    private String id;
    private List<Stop> stops;

    /**
     * 
     * @param id
     *            Bahnhofs-ID
     * @param name
     *            Name des Bahnhofs
     */
    public Station(String id, String longName, String shortName) {
    	if(id==null || shortName==null || longName==null) throw new IllegalArgumentException();
    	this.longName = longName;
    	this.shortName = shortName;
    	this.id = id;
    	this.stops = new LinkedList<Stop>();
    }
    
    /**
     * Einfacher Konstruktor für Station. LongName und ShortName sind hier identisch.
     * @param id
     * @param name
     */
    public Station(String id, String name){
    	this(id,name,name);
    }

    /**
     * Fügt den gegebenen Halt {@link Stop} zur Liste aller Halte dieses
     * Bahnhofs hinzu.
     * 
     * @param stop
     */
    public void addStop(Stop stop) {
    	if (stop == null)
    		throw new IllegalArgumentException();
    	stops.add(stop);
    }

    /**
     * 
     * @return (Langer) Name des Bahnhofs
     */
    public String getLongName() {
    	return this.longName;
    }
    
    /**
     * 
     * @return Kurzname des Bahnhofs
     */
    public String getShortName(){
    	return this.shortName;
    }

    /**
     * 
     * @return ID des Bahnhofs
     */
    public String getId() {
    	return this.id;
    }

    /**
     * @return Liste von allen Halten an diesem Bahnhof. Zum Aufruf aus der GUI,
     *         besser {TimetableController.getStopsByStation} benutzen, da dort
     *         auf null überprüft wird.
     */
    public List<Stop> getStops() {
    	return this.stops;
    }

    /**
     * Überprüft, ob der gegebene Halt an diesem Bahnhof ist.
     * 
     * @param stop
     *            Der Halt.
     * @return True, wenn der Halt an diesem Bahnhof ist, sonst False
     */
    public boolean hasStop(Stop stop) {
    	return stops.contains(stop);
    }

    /**
     * Entfernt den gegebenen Halt (sofern möglich). Ruft wegen der
     * Datenkonsistenz {@link Stop.deleteStop} auf.
     * 
     * @param stop
     *            Halt, der entfernt werden soll.
     */
    public void removeStop(Stop stop) {
    	if (stop != null)
    		stop.deleteStop();
    }

    @Override
    public String toString() {
    	return String.format("[%s] %s", id, longName);
    }
}
