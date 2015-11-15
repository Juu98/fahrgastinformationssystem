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
	 * adding default attributes to the model.
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
		return "redirect:/dep";
	}
	
	/**
	 * Departures display page.
	 * @param model
	 * @param form
	 * @return 
	 */
	@RequestMapping("/dep")
	public String dep(Model model, FilterForm form){
		defaults(model);
		
		Station currentStation = null;
		if(form.getStationId() != null){
			currentStation = this.timetable.getData().getStationByID(form.getStationId());
		}
		
		model.addAttribute("trains", this.timetable.filterByStation(
			this.timetable.getData().getTrainRoutes(),
			currentStation));
		model.addAttribute("form", form);
		
		model.addAttribute("stations", this.timetable.getData().getStations());
		model.addAttribute("currentStation", currentStation);
		
		return "dep";
	}
	
	/**
	 * TrainRoute display page.
	 * @param model
	 * @param formTR
	 * @return 
	 */
	@RequestMapping("trn")
	public String trn(Model model, TrainRouteForm formTR){
		defaults(model);
		
		TrainRoute currentTrainRoute = null;
		if (formTR.getTrainRouteId() != null){
			currentTrainRoute = this.timetable.getData().getTrainRouteById(formTR.getTrainRouteId());
		}
		
		model.addAttribute("trainRoutes", this.timetable.getData().getTrainRoutes());
		model.addAttribute("currentTrainRoute", currentTrainRoute);
		
		return "trn";
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
