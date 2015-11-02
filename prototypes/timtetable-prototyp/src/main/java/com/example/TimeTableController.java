package com.example;

import java.time.*;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class TimeTableController {
	//declaration of member variables
	private List<Station> stations;
	private Station currentStation;
	private String currentStationName;
	//"constructor"
	@PostConstruct
	public void Constructor()
	{	
		//initialize member variables
		this.stations = new ArrayList<Station>();
		this.currentStation = null;
		this.currentStationName = "";
		this.initData();
	}
	//initialize some data
	public void initData()
	{
		//create dummy stations
		Station dresden = new Station("Dresden", null);
		Station tharandt = new Station("Tharandt", null);
		Station freiberg = new Station("Freiberg", null);
		//create dummy stops for dresden
		Stop dresden_1 = new Stop(dresden, 1, LocalDateTime.of(2015, 11, 1, 10, 53),  LocalDateTime.of(2015, 11, 1, 10, 55), freiberg);
		Stop dresden_2 = new Stop(dresden, 2, LocalDateTime.of(2015, 11, 1, 11, 53),  LocalDateTime.of(2015, 11, 1, 11, 55), freiberg);
		//create dummy stops for tharandt
		Stop tharandt_1 = new Stop(tharandt, 1, LocalDateTime.of(2015, 11, 1, 11, 13),  LocalDateTime.of(2015, 11, 1, 11, 15), freiberg);
		Stop tharandt_2 = new Stop(tharandt, 1, LocalDateTime.of(2015, 11, 1, 16, 13),  LocalDateTime.of(2015, 11, 1, 16, 15), dresden);
		Stop tharandt_3 = new Stop(tharandt, 2, LocalDateTime.of(2015, 11, 1, 12, 13),  LocalDateTime.of(2015, 11, 1, 12, 15), freiberg);
		Stop tharandt_4 = new Stop(tharandt, 2, LocalDateTime.of(2015, 11, 1, 17, 13),  LocalDateTime.of(2015, 11, 1, 17, 15), dresden);
		//create dummy stops for dresden
		Stop freiberg_1 = new Stop(freiberg, 1, LocalDateTime.of(2015, 11, 1, 12, 53),  LocalDateTime.of(2015, 11, 1, 12, 55), dresden);
		Stop freiberg_2 = new Stop(freiberg, 2, LocalDateTime.of(2015, 11, 1, 15, 53),  LocalDateTime.of(2015, 11, 1, 15, 55), dresden);
		//create dummy trips
		List<Stop> lDresden = new LinkedList<Stop>();
		List<Stop> lTharandt = new LinkedList<Stop>();
		List<Stop> lFreiberg = new LinkedList<Stop>();
		//fill dummy stop lists
		lDresden.add(dresden_1);
		lDresden.add(dresden_2);
		lTharandt.add(tharandt_1);
		lTharandt.add(tharandt_2);
		lTharandt.add(tharandt_3);
		lTharandt.add(tharandt_4);
		lFreiberg.add(freiberg_1);
		lFreiberg.add(freiberg_2);
		//add the stop lists to their stations
		dresden.appendStops(lDresden);
		tharandt.appendStops(lTharandt);
		freiberg.appendStops(lFreiberg);
		//add stations to the stations member variable
		this.stations.add(dresden);
		this.stations.add(tharandt);
		this.stations.add(freiberg);
	}
	//fill input form and send the requested result
	@RequestMapping(method = RequestMethod.GET)
	public String showTimeTable(Model model, StationForm stationForm)
	{
		//show error messages if no stations are found or if the entered station isn't found
		if(this.stations.isEmpty())
		{
			model.addAttribute("stationMessage", "No Stations found!");
		}
		else
		{
			if(this.currentStation == null && !this.currentStationName.isEmpty())
				model.addAttribute("stationMessage", "Station not found!");
			else if(this.currentStation != null && !this.currentStationName.isEmpty())
				model.addAttribute("stationMessage", "");
		}
		//bind attributes
		model.addAttribute("currentStationName", this.currentStationName);
		//bind table data, if it is available
		if(this.currentStation != null)
			model.addAttribute("stops", currentStation.getStops());
		return "timetable";
	}
	//get the station data from the form and transfer it to the local member variables
	@RequestMapping(method = RequestMethod.POST)
	public String getStation(@Valid StationForm stationForm, Model model)
	{
		if(!this.stations.isEmpty())
		{
			for(Station station : this.stations)
			{
				if(station.getName().equals(stationForm.getCurrentStationName()))
				{
					this.currentStation = station;
					break;
				}
				else
				{
					currentStation = null;
				}
			}
		}
		this.currentStationName = stationForm.getCurrentStationName();
		return "redirect:/";
	}
}
