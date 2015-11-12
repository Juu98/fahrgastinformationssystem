/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fis.web;

import java.time.LocalTime;

/**
 * Interface mapping data from the filter form.
 * @author Robert
 */
public interface FilterForm {
	public String getStationId();
	public String getStationName();
	public String getDestination();
	public LocalTime getStart();
	public LocalTime getEnd();
}
