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
	private int posX = -1; //-1, wenn kein Wert gesetzt
    private int posY = -1; //-1, wenn kein Wert gesetzt

	/**
	 * Standard-Konstruktor.
	 * @param id		Bahnhofs-ID
	 * @param longName	Name des Bahnhofs
	 * @param shortName	Abkürzung des Bahnhofsnamens
	 * @param x			X-Koordinate
	 * @param y			Y-Koordinate
	 */
	public Station(String id, String longName, String shortName, int x, int y) {
		if (id == null || shortName == null || longName == null)
			throw new IllegalArgumentException();
		this.longName = longName;
		this.shortName = shortName;
		this.id = id;
		this.stops = new LinkedList<>();
		this.posX = x;
		this.posY = y;
	}
	
	/**
	 * Einfacher Konstruktor für Station.
	 * Koordinaten sind hier {@literal -1}
	 * @param id
	 * @param longName
	 * @param shortName 
	 */
	public Station(String id, String longName, String shortName) {
		this(id, longName, shortName, -1, -1);
	}

	/**
	 * Einfacher Konstruktor für Station.
	 * LongName und ShortName sind hier identisch und Koordinaten {@literal -1}.
	 * 
	 * @param id
	 * @param name
	 */
	public Station(String id, String name) {
		this(id, name, name);
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
	public String getShortName() {
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

	/**
	 * Funktion zum bestimmen des Stops einer Station in einem Zuglauf.
	 * 
	 * @param station
	 * @return die gefundene Station; sonst {@literal null}
	 * @throws IllegalArgumentException
	 */
	public Stop getStopForTrainRoute(TrainRoute route) throws IllegalArgumentException {
		if (route == null)
			throw new IllegalArgumentException();
		for (Stop stop : this.getStops()) {
			//skip the stop if it doesn't contain a train route
			if(stop.getTrainRoute() == null)
				continue;
			if ((stop.getTrainRoute().equals(route)) && (stop.getStopType().equals(StopType.BEGIN)
					|| stop.getStopType().equals(StopType.END) || stop.getStopType().equals(StopType.STOP)))
				return stop;
		}
		return null;
	}
	
	/**
	 * Festlegen der X-Koordinate
	 * @param posX
	 */
	public void setPosX(float posX){
		this.posX=posX;
	}
	
	/**
	 * Festlegen der Y-Koordinate
	 * @param posY
	 */
	public void setPosY(float posY){
		this.posY=posY;
	}
	
	/**
	 * 
	 * @return X-Koordinate des Bahnhofs
	 */
	public float getPosX(){
		return this.posX;
	}
	
	/**
	 * 
	 * @return Y-Koordinate des Bahnhofs
	 */
	public float getPosY(){
		return this.posY;
	}

	@Override
	public String toString() {
		return String.format("[%s] %s", id, longName);
	}
}
