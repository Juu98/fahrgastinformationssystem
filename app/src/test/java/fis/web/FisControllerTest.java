package fis.web;

import fis.common.CommonConfig;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import fis.FilterTime;
import fis.FilterType;
import fis.data.Station;
import fis.data.Stop;
import fis.data.StopType;
import fis.data.TimetableController;
import fis.data.TimetableData;
import fis.data.TrainCategory;
import fis.data.TrainRoute;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FisControllerTest {
	private FisController controller;
	private TimetableController ttc;
	private Station station;
	private Station drr;
	private TrainRoute tp_9801;
	private TrainRoute tp_9802;
	private List<TrainRoute> expectedTrainRoutesDeparture;
	private List<TrainRoute> expectedTrainRoutesArrival;
	private CommonConfig config;

	@Before
	public void setup() throws NullPointerException{
		this.config = new CommonConfig();
		config.setRailmlpath("EBL Regelfahrplan.xml");
		this.ttc = new TimetableController(this.config);
		this.ttc.resetData();
		this.ttc.loadML();
		this.controller = new FisController(this.ttc);
		this.station = this.ttc.getData().getStations().get(0);
		
		for(Station s : this.ttc.getStations()){
			if(s.getId().equals("ocp_DRR")){
				this.drr = s;
				break;
			}
		}
		if(this.drr == null)
			throw new NullPointerException("Station DRR not found!");
		
		for(TrainRoute route : this.ttc.getData().getTrainRoutes())
		{
			if(route.getId().equals("tp_9801")){
				this.tp_9801 = route;
				break;
			}
		}
		if(this.tp_9801 == null)
			throw new NullPointerException("Route TP_9801 not found!");
		
		this.expectedTrainRoutesDeparture = new ArrayList<TrainRoute>();
		this.expectedTrainRoutesDeparture.add(tp_9801);
		
		for(TrainRoute route : this.ttc.getData().getTrainRoutes())
		{
			if(route.getId().equals("tp_9802")){
				this.tp_9802 = route;
				break;
			}
		}
		if(this.tp_9802 == null)
			throw new NullPointerException("Route TP_9802 not found!");
		
		this.expectedTrainRoutesArrival = new ArrayList<TrainRoute>();
		this.expectedTrainRoutesArrival.add(tp_9802);
	}

	@Test
	public void filterStationNullTest(){
		assertArrayEquals(this.controller.filter(null, FilterType.ARRIVAL, LocalTime.of(13, 37), LocalTime.of(13, 38), FilterTime.ACTUAL).toArray(), new ArrayList<TrainRoute>().toArray());
	}

	@Test(expected = IllegalArgumentException.class)
	public void filterFilterTypeTest(){
		this.controller.filter(this.station, null, LocalTime.of(13, 37), LocalTime.of(13, 38), FilterTime.ACTUAL);
	}

	@Test(expected = IllegalArgumentException.class)
	public void filterFromNullTest(){
		this.controller.filter(this.station, FilterType.ARRIVAL, null, LocalTime.of(13, 38), FilterTime.ACTUAL);
	}

	@Test(expected = IllegalArgumentException.class)
	public void filterToNullTest(){
		this.controller.filter(this.station, FilterType.ARRIVAL, LocalTime.of(13, 37), null, FilterTime.ACTUAL);
	}

	@Test(expected = IllegalArgumentException.class)
	public void filterFilterTimeNullTest(){
		this.controller.filter(this.station, FilterType.ARRIVAL, LocalTime.of(13, 37), LocalTime.of(13, 38), null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void filterAnyTest(){
		this.controller.filter(this.station, FilterType.ANY, LocalTime.of(13, 37), LocalTime.of(13, 38), FilterTime.ACTUAL);
	}
	
	@Test
	public void filterDepartureTest(){
		assertArrayEquals(this.controller.filter(drr, FilterType.DEPARTURE, LocalTime.of(7, 00), LocalTime.of(8, 00), FilterTime.SCHEDULED).toArray(), this.expectedTrainRoutesDeparture.toArray());
	}
	
	@Test
	public void filterArrivalTest(){
		assertArrayEquals(this.controller.filter(drr, FilterType.ARRIVAL, LocalTime.of(8, 00), LocalTime.of(9, 00), FilterTime.SCHEDULED).toArray(), this.expectedTrainRoutesArrival.toArray());
	}
}
