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
    private String name;
    private String id;
    private List<Stop> stops;

    /**
     * 
     * @param id
     *            Bahnhofs-ID
     * @param name
     *            Name des Bahnhofs
     */
    public Station(String id, String name) {
	this.name = name;
	this.id = id;
	this.stops = new LinkedList<Stop>();
    }

    /**
     * Fügt den gegebenen Halt {@link Stop} zur Liste aller Halte dieses
     * Bahnhofs hinzu.
     * 
     * @param stop
     */
    public void addStop(Stop stop) throws NullPointerException {
	if (stop == null)
	    throw new NullPointerException();
	stops.add(stop);
    }

    /**
     * 
     * @return Name des Bahnhofs
     */
    public String getName() {
	return name;
    }

    /**
     * 
     * @return ID des Bahnhofs
     */
    public String getId() {
	return id;
    }

    /**
     * @return Liste von allen Halten an diesem Bahnhof. Zum Aufruf aus der GUI,
     *         besser {TimetableController.getStopsByStation} benutzen, da dort
     *         auf null überprüft wird.
     */
    public List<Stop> getStops() {
	return stops;
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
	return String.format("[%s] %s", id, name);
    }
}
