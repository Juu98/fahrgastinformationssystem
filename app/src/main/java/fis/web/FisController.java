/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fis.web;

import fis.Application;
import fis.FilterTime;
import fis.FilterType;
import fis.data.Station;
import fis.data.TimetableController;
import fis.data.TrainRoute;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Main Controller class for the FIS application.
 * @author Robert
 */
@Controller
public class FisController {
	private final TimetableController timetable;
	private static final Logger LOGGER = Logger.getLogger(FisController.class);
	
	/**
	 * TODO: add paramteres
	 * @param tt
	 */
	@Autowired
	public FisController(TimetableController tt){
		this.timetable = tt;
	}
	
	/**
	 * Adds default attributes to the model.
	 * @param model 
	 */
	public void defaults(Model model){
		model.addAttribute("time", this.timetable.getTime());
		model.addAttribute("connState", this.timetable.getStateName());
	}
	
	/**
	 * sets Landing page to departures
	 * @return 
	 */
	@RequestMapping({"/", "/fis"})
	public String index(){
		return "redirect:/dep/";
	}
	
	/**
	 * Departures display page for no given station.
	 * @param model		the {@link Application}'s model
	 * @param form		the {@link FilterForm} submitted by the user
	 * @return the empty departures page
	 */
	@RequestMapping("/dep/")
	public String depDefault(Model model, FilterForm form){
		return dep(model, form, null);
	}
	
	/**
	 * Redirection to only use valid URIs.
	 * @return 
	 */
	@RequestMapping("/dep")
	public String depRedir(){
		return "redirect:/dep/";
	}
	
	/**
	 * Departures display page.
	 * @param model		the {@link Application}'s model
	 * @param form		the {@link FilterForm} submitted by the user
	 * @param stn		the currently selected {@link Station}'s ID
	 * @return the departures page read from {@linkplain src/main/resources/templates/dep.html}
	 */
	@RequestMapping("/dep/{stn}")
	public String dep(Model model, FilterForm form, @PathVariable("stn") String stn){
		// add default parameters to the model
		defaults(model);
		
		// get current station from URL or Form
		Station currentStation = null;
		if (stn != null && !stn.isEmpty()){
			currentStation = this.timetable.getData().getStationById(stn);
		}
		if(form.getStationId() != null){
			currentStation = this.timetable.getData().getStationById(form.getStationId());
		}
		LOGGER.debug("*DEP* Current station: " + currentStation);
		
		// Add all trains containing the given station as departure to the model
		model.addAttribute("comp", new TrainRouteComparator(currentStation, FilterTime.SCHEDULED, FilterType.DEPARTURE));

		model.addAttribute("trains", this.timetable.getTrainRoutesByStation(currentStation,FilterType.DEPARTURE));
		model.addAttribute("form", form);
		
		model.addAttribute("stations", this.timetable.getData().getStations());
		model.addAttribute("currentStation", currentStation);
		
		return "dep";
	}
	
	/**
	 * Arrivals display page for no given station.
	 * @param model		the {@link Application}'s model
	 * @param form		the {@link FilterForm} submitted by the user
	 * @return the empty departures page
	 */
	@RequestMapping("/arr/")
	public String arrDefault(Model model, FilterForm form){
		return arr(model, form, null);
	}
	
	/**
	 * Redirection to only use valid URIs.
	 * @return 
	 */
	@RequestMapping("/arr")
	public String arrRedir(){
		return "redirect:/arr/";
	}
	
	/**
	 * Arrivals display page.
	 * @param model		the {@link Application}'s model
	 * @param form		the {@link FilterForm} submitted by the user
	 * @param stn		the currently selected {@link Station}
	 * @return the arrivals page read from {@linkplain src/main/resources/templates/arr.html}
	 */
	@RequestMapping("/arr/{stn}")
	public String arr(Model model, FilterForm form, @PathVariable("stn") String stn){
		// add default parameters to the model
		defaults(model);
		
		// get current station from URL or Form
		Station currentStation = null;
		if (stn != null && !stn.isEmpty()){
			currentStation = this.timetable.getData().getStationById(stn);
		}
		if(form.getStationId() != null){
			currentStation = this.timetable.getData().getStationById(form.getStationId());
		}
		LOGGER.debug("*ARR* Current station: " + currentStation);
		
		// Add all trains containing the given station as departure to the model
		model.addAttribute("comp", new TrainRouteComparator(currentStation, FilterTime.SCHEDULED, FilterType.ARRIVAL));

		model.addAttribute("trains", this.timetable.getTrainRoutesByStation(currentStation,FilterType.ARRIVAL));
		model.addAttribute("form", form);
		
		model.addAttribute("stations", this.timetable.getData().getStations());
		model.addAttribute("currentStation", currentStation);
		
		return "arr";
	}
	
	/**
	 * Arrivals display page for no given station.
	 * @param model		the {@link Application}'s model
	 * @param formTR	the {@link FilterForm} submitted by the user
	 * @return the empty departures page
	 */
	@RequestMapping("/trn/")
	public String trnDefault(Model model, TrainRouteForm formTR){
		return trn(model, formTR, null);
	}
	
	/**
	 * Redirection to only use valid URIs.
	 * @return 
	 */
	@RequestMapping("/trn")
	public String trnRedir(){
		return "redirect:/trn/";
	}
	
	/**
	 * Train display page.
	 * @param model		the {@link Application}'s model
	 * @param formTR	the {@link TrainRouteForm} submitted by the user
	 * @param trt		the currently selected {@link TrainRoute}
	 * @return the arrivals page read from {@linkplain src/main/resources/templates/trn.html}
	 */
	@RequestMapping("/trn/{trt}")
	public String trn(Model model, TrainRouteForm formTR, @PathVariable("trt") String trt){
		// add default parameters to the model
		defaults(model);
		
		// get current train route from URL or Form
		TrainRoute currentTrainRoute= null;
		if (trt != null && !trt.isEmpty()){
			currentTrainRoute = this.timetable.getData().getTrainRouteById(trt);
		}
		if(formTR.getTrainRouteId() != null){
			currentTrainRoute = this.timetable.getData().getTrainRouteById(formTR.getTrainRouteId());
		}
		LOGGER.debug("*TRN* Current train route: " + currentTrainRoute);
		
		model.addAttribute("trainRoutes", this.timetable.getData().getTrainRoutes());
		model.addAttribute("currentTrainRoute", currentTrainRoute);
		
		return "trn";
	}
	
	/**
	 * JSON list with all {@link Station}s for the typeahead.
	 * @return 
	 */
	@RequestMapping("stations.json")
	public @ResponseBody List<JSONProvider.StationView> getStations(){
		return new JSONProvider().getStations(this.timetable.getData().getStations());
	}
	
	/**
	 * JSON list with all {@link TrainRoute}s for the typeahead.
	 * @return 
	 */
	@RequestMapping("trainRoutes.json")
	public @ResponseBody List<JSONProvider.TrainRouteView> getTrainRoutes(){
		return new JSONProvider().getTrainRoutes(this.timetable.getData().getTrainRoutes());
	}
}
