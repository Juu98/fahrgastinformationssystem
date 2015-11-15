/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fis.web;

import fis.Station;
import fis.Timetable;

import fis.TrainRoute;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Main Controller class for the FIS application.
 * @author Robert
 */
@Controller
public class FisController {
	private final Timetable timetable;
	
	/**
	 * TODO: add paramteres
	 * @param tt
	 */
	@Autowired
	public FisController(Timetable tt){
		this.timetable = tt;
	}
	
	/**
	 * sets Landing page to fis.html
	 * @return 
	 */
	@RequestMapping("/")
	public String index(){
		return "redirect:/fis";
	}
	
	/**
	 * Landing page.
	 * @param model
	 * @param form
	 * @return 
	 */
	@RequestMapping("/fis")
	public String fis(Model model, FilterForm form, TrainRouteForm formTR){
		Station currentStation = null;
		if(form.getStationId() != null){
			currentStation = this.timetable.getData().getStationByID(form.getStationId());
		}
		
		TrainRoute currentTrainRoute = null;
		if (formTR.getId() != null){
			currentTrainRoute = this.timetable.getData().getTrainRouteById(formTR.getId());
		}
		
		/*if (currentStation == null || currentStation.isEmpty()){
			currentStation = "HBF";
		}*/ // init station for mockup
		model.addAttribute("time", this.timetable.getTime());
		model.addAttribute("connState", this.timetable.getStateName());
		model.addAttribute("trains", this.timetable.filterByStation(
			this.timetable.getData().getTrainRoutes(),
			currentStation));
		model.addAttribute("form", form);
		
		model.addAttribute("stations", this.timetable.getData().getStations());
		model.addAttribute("currentStation", currentStation);
	
		model.addAttribute("trainRoutes", this.timetable.getData().getTrainRoutes());
		model.addAttribute("currentTrainRoute", currentTrainRoute);
		return "fis";
	}
	
	/**
	 * JSON list with all stations for the typeahead
	 * @return 
	 */
	@RequestMapping("stations.json")
	public @ResponseBody List<Station> getStations(){
		return this.timetable.getData().getStations();
	}
	
	@RequestMapping("trainRoutes.json")
	public @ResponseBody List<TrainRoute> getTrainRoutes(){
		return this.timetable.getData().getTrainRoutes();
	}
}
