package com.example;

import java.util.*;

public class Station {
	//declaration of member variables
	private String name;
	private List<Stop> stops;
	//constructor
	public Station(String name, List<Stop> stops) throws NullPointerException, IllegalArgumentException
	{
		//abort, if the name is invalid
		if(name == null)
			throw new NullPointerException();
		if(name == "")
			throw new IllegalArgumentException();	
		//set member variables
		this.name = name;
		this.stops = new LinkedList<Stop>();
		//append stops, if they exist
		if(stops != null)
			this.stops.addAll(stops);
	}
	//appends stops to the station
	public void appendStops(List<Stop> stops) throws NullPointerException
	{
		if(stops == null)
			throw new NullPointerException();
		this.stops.addAll(stops);
	}
	//checks, if a specific stop is at the station
	public boolean hasStop(Stop stop) throws NullPointerException
	{
		if(stop == null)
			throw new NullPointerException();
		return this.stops.contains(stop);
	}
	//getter for stops
	public List<Stop> getStops()
	{
		return this.stops;
	}
	//getter for Name
	public String getName()
	{
		return this.name;
	}
}
