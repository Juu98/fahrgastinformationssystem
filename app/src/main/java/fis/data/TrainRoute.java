package fis.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Eine Klasse für einen Zuglauf
 * @author Luux
 *
 */
public class TrainRoute {
	private String id;
	private int trainNumber;
	private TrainCategory trainCategory;
	private List<Stop> stops=new ArrayList<Stop>();
	
	/**
	 * @param id Zuglauf-ID
	 * @param trainNumber Zugnummer
	 * @param trainCategory Zugkategorie. Siehe {@link TrainCategory}.
	 * @param stops List von allen {@link Stop}s dieses Zuglaufs.
	 */
	public TrainRoute(String id,int trainNumber, TrainCategory trainCategory, List<Stop> stops){
		this.id=id;
		this.trainNumber=trainNumber;
		this.trainCategory=trainCategory;
		this.stops=stops;
		
		//link stops
		for(Stop stop:stops){
			linkStop(stop);
		}
	}
	
	/**
	 * @return ID des Zuglaufs
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Verlinkt den gegebenen Stop zu diesem Zuglauf
	 * @param stop
	 */
	public void linkStop(Stop stop){
		if(stop!=null) stop.setTrainRoute(this);
	}
	
	/**
	 * @return Zugnummer
	 */
	public int getTrainNumber(){
		return trainNumber;
	}
	
	/**
	 * @return Zugkategorie. Siehe {@link TrainCategory}.
	 */
	public TrainCategory getTrainCategory(){
		return trainCategory;
	}
	
	/**
	 * @return Eine Liste von allen {@link Stop}s dieses Zuglaufs
	 */
	public List<Stop> getStops(){
		return stops;
	}
	
	/**
	 * 
	 * @return Das Ende des Zuglaufs
	 */
	public Stop getLastStop(){
		return stops.get(stops.size()-1);
	}
	
	/**
	 * 
	 * @return Den Anfang des Zuglaufs
	 */
	
	public Stop getFirstStop(){
		return stops.get(0);
	}
	
	/**
	 * Liefert eine menschenlesbare Zugnummer.
	 * @return Zugnummer in der Form ICE 123
	 */
	@Override
	public String toString(){
		if (this.trainCategory != null) return String.format("%s %s", this.trainCategory.getName(), this.trainNumber);
		else return this.id;
	}
}
