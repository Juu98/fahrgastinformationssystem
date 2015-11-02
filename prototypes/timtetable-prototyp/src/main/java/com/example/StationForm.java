package com.example;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.NotBlank;

public class StationForm {
	//TODO: fix the @NotBull for the departure checkbox
	//declaration of member variables
	@NotNull
	@NotBlank
	private String currentStationName;
	//getter for currentStationName
	public String getCurrentStationName()
	{
		if(this.currentStationName == null)
			return "";
		return this.currentStationName;
	}
	//setter for currentStationName
	//TODO: throw Exceptions again?
	public void setCurrentStationName(String currentStationName) throws NullPointerException
	{
		if(this.currentStationName == null)
			this.currentStationName = "";
			//throw new NullPointerException();
		this.currentStationName = currentStationName;
	}
}
