package fis.data;

import java.time.LocalTime;

/**
 * Represents a specific stop
 * @author Eric
 *
 */
public class Stop {
	private Station station;
	private LocalTime ScheduledDeparture;
	private LocalTime ScheduledArrival;
	private LocalTime ActualArrival;
	private LocalTime ActualDeparture;
	
	private String ScheduledTrack;
	private String ActualTrack;
	private String Message; //Hier bin ich noch unsicher, was dort genau rein soll
	private StopType stopType;
	private TrainRoute trainRoute;

	/**
	 * Creates a new Stop
	 * @param station The Station where the train stops
	 * @param stopType {@link StopType} of the stop
	 * @param ScheduledArrival Scheduled Arrival of the train
	 * @param ScheduledDeparture Scheduled Departure of the train
	 * @param ScheduledTrack Scheduled Track where the train arrives at the station
	 */
	public Stop(Station station,StopType stopType,LocalTime ScheduledArrival, LocalTime ScheduledDeparture, String ScheduledTrack){
		this.station=station;
		this.stopType=stopType;
		this.ScheduledArrival=ScheduledArrival;
		this.ScheduledDeparture=ScheduledDeparture;
		this.ScheduledTrack=ScheduledTrack;	
		
		this.ActualArrival=ScheduledArrival;
		this.ActualDeparture=ScheduledDeparture;
		this.ActualTrack=ScheduledTrack;
		
		station.addStop(this);	
	}
	
	/**
	 * Getter for station
	 * @return The {@link Station} where the train stops.
	 */
	public Station getStation(){
		return station;
		}
	
	/**
	 * Sets the Train Route where this stops occurs
	 * @param route The {@link TrainRoute} that contains this stop
	 */
	public void setTrainRoute(TrainRoute route){
		this.trainRoute=route;
	}
	
	/**
	 * Cleans the connection of the Train Route and the Station with this stop.
	 * Do use if you want to remove this stop for data consistency. Should normally be executed by {@link Station.removeStop} or {@link TrainRoute.removeStop}
	 */
	public void deleteStop(){
		if(trainRoute!=null){
			trainRoute.getStops().remove(this);
		}
		
		if(station!=null){
			station.getStops().remove(this);
		}
		
	}

	/**
	 * Getter for stopType
	 * @return The stored {@link StopType} for this stop
	 */
	public StopType getStopType(){
		return stopType;
	}
	
	
	/**
	 * Updates the stop type of this stop (use for update telegrams)
	 * @param newStopType The new {@link StopType}
	 */
	public void updateStopType(StopType newStopType){
		this.stopType=newStopType;
	}
	
	/**
	 * Updates the actual arrival time of the train (use for update telegrams).
	 * @param ActualArrival The new actual arrival time.
	 */
	public void updateArrival(LocalTime ActualArrival){
		this.ActualArrival=ActualArrival;
	}
	
	/**
	 * Updates the actual departure time of the train (use for update telegrams).
	 * @param ActualDeparture The new actual departure time.
	 */
	public void updateDeparture(LocalTime ActualDeparture){
		this.ActualDeparture=ActualDeparture;
	}
	
	
	/**
	 * Updates the actual track where the train arrives at the station (use for update telegrams).
	 * @param ActualTrack The new actual track
	 */
	public void updateTrack(String ActualTrack){
		this.ActualTrack=ActualTrack;
	}
	
	/**
	 * @return Scheduled Departure
	 */
	public LocalTime getScheduledDeparture(){return ScheduledDeparture;}
	
	/** 
	 * @return Scheduled Arrival
	 */
	public LocalTime getScheduledArrival(){return ScheduledArrival;}
	
	/**
	 * @return Actual Departure
	 */
	public LocalTime getActualDeparture(){return ActualDeparture;}
	
	/**
	 * @return Actual Arrival
	 */
	public LocalTime getActualArrival(){return ActualArrival;}
	
	/**
	 * @return Scheduled Track
	 */
	public String getScheduledTrack(){return ScheduledTrack;}
	
	/**
	 * @return Actual Track
	 */
	public String getActualTrack(){return ActualTrack;}
	
	/**
	 * @return The TrainRoute containing this stop. Note that this has to be set before!
	 */
	public TrainRoute getTrainRoute(){
		return trainRoute;
	}

	public void printDebugInformation(){
		//erstmal nur zum Testen
		System.out.println("---");
		if(station!=null){
			System.out.println("Station: "+station.getName());
		} else {
			System.out.println("!!STATION: NULL!!");
		}
		System.out.println("Train Number: ");
		System.out.println("Scheduled Arrival: "+ScheduledArrival);
		System.out.println("Scheduled Departure: "+ScheduledDeparture);
		System.out.println("Scheduled Track: "+ScheduledTrack);
	}
	
	
	
}
