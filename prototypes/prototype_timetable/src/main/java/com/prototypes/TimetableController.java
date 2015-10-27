package com.prototypes;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TimetableController {
    @RequestMapping("/timetable")
    public String timetable(Model model) throws IOException {
	ApplicationContext appContext = new ClassPathXmlApplicationContext("Beans.xml");
	XMLConverter converter = (XMLConverter) appContext.getBean("XMLConverter");
	Timetable timetable1 = (Timetable) converter.convertFromXMLToObject("trains.xml");
	model.addAttribute("trains", timetable1.getTrains());
	model.addAttribute("timetable1", timetable1);
	return "timetable";
    }
}
