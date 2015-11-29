/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fis.web;

import fis.FilterTime;
import fis.FilterType;
import fis.data.Station;
import fis.data.Stop;
import fis.data.TrainRoute;
import java.time.LocalTime;
import java.util.Comparator;

/**
 * {@link Comparator} that compares two given {@link TrainRoute}s
 * by the time they are present at a given {qlink station}.
 * 
 * <p> In the event of a train stopping twice at the same station, the first stop is taken into account.
 * @author Robert
 */
public class TrainRouteComparator implements Comparator<TrainRoute>{
	private final Station station;
	private final FilterTime filterTime;
	private final FilterType filterType;
	
	/**
	 * Default constructor.
	 * @param station		The {@link Station} at which the times should be compared
	 * @param filterTime	Whether to compare the scheduled or actual times
	 * @param filterType	Wheter to compare arrival or departure times,
	 *						{@link FilterType.ANY} not supported here.
	 */
	public TrainRouteComparator(Station station, FilterTime filterTime, FilterType filterType){
		super();
		
		this.station = station;
		this.filterTime = filterTime;
		
		if (filterType == FilterType.ANY) throw new IllegalArgumentException(
			"FilterType.ANY not supported here, choose either ARRIVAL or DEPARTURE.");
		this.filterType = filterType;		
	}
	
	/**
	 * Compares two {@link TrainRoute}s in the manner defined when creating this Comparator.
	 * @param o1	first {@link TrainRoute}
	 * @param o2	second {@link TrainRoute}
	 * @return -1	if the first {@link TrainRoute} is present BEFORE the second
	 *			0	if they are present TOGETHER
	 *			1	if the first {@link TrainRoute} is present AFTER
	 */
	@Override
	public int compare(TrainRoute o1, TrainRoute o2) {
		LocalTime t1 = null, t2 = null;
		
		// first stop
		for (Stop s : o1.getStops()){
			if (!s.getStation().equals(this.station)) continue;
			
			if (this.filterType == FilterType.DEPARTURE){
				if (this.filterTime == FilterTime.ACTUAL) t1 = s.getActualDeparture();
				else t1 = s.getScheduledDeparture();
			} else {
				if (this.filterTime == FilterTime.ACTUAL) t1 = s.getActualArrival();
				else t1 = s.getScheduledArrival();
			}
		}
		if (t1 == null) throw new IllegalArgumentException(
			String.format("First TrainRoute %s doesn't stop at %s!", o1, this.station));
		
		// second stop
		for (Stop s : o2.getStops()){
			if (!s.getStation().equals(this.station)) continue;
			
			if (this.filterType == FilterType.DEPARTURE){
				if (this.filterTime == FilterTime.ACTUAL) t2 = s.getActualDeparture();
				else t2 = s.getScheduledDeparture();
			} else {
				if (this.filterTime == FilterTime.ACTUAL) t2 = s.getActualArrival();
				else t2 = s.getScheduledArrival();
			}
		}
		if (t2 == null) throw new IllegalArgumentException(
			String.format("Second TrainRoute %s doesn't stop at %s!", o2, station));
		
		return t1.compareTo(t2);
	}
	
}
