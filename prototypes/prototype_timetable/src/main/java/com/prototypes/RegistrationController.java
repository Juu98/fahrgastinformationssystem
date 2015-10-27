package com.prototypes;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegistrationController {
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registrationForm(Model model) {
	model.addAttribute("train", new Train());
	return "train_registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registrationSubmit(@ModelAttribute Train train, Model model) throws IOException {
	ApplicationContext appContext = new ClassPathXmlApplicationContext("Beans.xml");
	XMLConverter converter = (XMLConverter) appContext.getBean("XMLConverter");
	Timetable timetable1 = (Timetable) converter.convertFromXMLToObject("trains.xml");
	// TODO add checks for string/int
	timetable1.getTrains().add(train);
	converter.convertFromObjectToXML(timetable1, "trains.xml");
	train = new Train();
	return "redirect:/timetable";
    }
}
