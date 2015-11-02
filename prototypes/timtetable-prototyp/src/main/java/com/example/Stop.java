package com.example;

import java.time.LocalDateTime;

public class Stop {
	//declaration of member variables
	private Station station;
	private Station destination;
	private int platform;
	private LocalDateTime arrival;
	private LocalDateTime departure;
	//constructor
	public Stop(Station station, int platform, LocalDateTime arrival, LocalDateTime departure, Station destination) throws NullPointerException, IllegalArgumentException
	{
		//abort if station, arrival or departure is invalid
		if(station == null || arrival == null || departure == null || destination == null)
			throw new NullPointerException();	
		if(arrival.compareTo(departure) > 0)
			throw new IllegalArgumentException();
		//set member variables
		this.station = station;
		this.platform = platform;
		this.arrival = arrival;
		this.departure = departure;
		this.destination = destination;
	}
	//getter for station
	public Station getStation()
	{
		return this.station;
	}
	//getter for platform
	public int getPlatform()
	{
		return this.platform;
	}
	//getter for arrival
	public LocalDateTime getArrival()
	{
		return this.arrival;
	}
	//getter for departure
	public LocalDateTime getDeparture()
	{
		return this.departure;
	}
	//getter for destination
	public Station getDestination()
	{
		return this.destination;
	}
}
