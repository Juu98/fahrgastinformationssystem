/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fis.web;

import fis.Station;
import fis.Timetable;
import fis.TimetableExample;
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
	public String fis(Model model, FilterForm form){
		Station currentStation=null;
		if(form.getStationId()!=null){
			currentStation= this.timetable.getData().getStationByID(form.getStationId());
		}
		
		/*if (currentStation == null || currentStation.isEmpty()){
			currentStation = "HBF";
		}*/ // init station for mockup
		model.addAttribute("time", this.timetable.getTime());
		model.addAttribute("connState", this.timetable.getStateName());
		if(currentStation!=null){
			model.addAttribute("trains", this.timetable.filterByStation(
				this.timetable.getData().getTrainRoutes(),
				currentStation));
		}
		model.addAttribute("form", form);
		model.addAttribute("stations", this.timetable.getData().getStations());
		model.addAttribute("currentStation", currentStation);
		
		
		return "fis";
	}
	
	@RequestMapping("stations.json")
	public @ResponseBody List<Station> getStations(){
		return this.timetable.getData().getStations();
	}
}
