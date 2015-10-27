package com.prototypes;

public class Train {
    public Train(String name, int number, String arrival, String departure, int platform) {
	super();
	this.name = name;
	this.number = number;
	this.arrival = arrival;
	this.departure = departure;
	this.platform = platform;
    }

    public Train() {
	// TODO Auto-generated constructor stub
    }

    private String name;
    private int number;
    private String arrival;
    private String departure;
    private int platform;

    public int getPlatform() {
	return platform;
    }

    public void setPlatform(int platform) {
	this.platform = platform;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public int getNumber() {
	return number;
    }

    @Override
    public String toString() {
	return "Train [name=" + name + ", number=" + number + ", arrival=" + arrival + ", departure=" + departure + "]";
    }

    public void setNumber(int number) {
	this.number = number;
    }

    public String getArrival() {
	return arrival;
    }

    public void setArrival(String arrival) {
	this.arrival = arrival;
    }

    public String getDeparture() {
	return departure;
    }

    public void setDeparture(String departure) {
	this.departure = departure;
    }

}
