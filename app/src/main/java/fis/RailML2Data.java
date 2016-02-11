/* Copyright 2016 Erik Schölzel, Robert Mörseburg, Zdravko Yanakiev, Jonas Schenke, Oliver Schmidt
 *
 * This file is part of FIS.
 *
 * FIS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FIS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FIS.  If not, see <http://www.gnu.org/licenses/>.
 */
package fis;

import fis.data.*;
import fis.railmlparser.RailMLParser;
import org.apache.log4j.Logger;
import org.railml.schemas._2009.*;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Eine Klasse zum konvertieren der RailML Daten in die intern genutzte Struktur. 
 * @author kloppstock
 */
public class RailML2Data {
	private static final Logger LOGGER = Logger.getLogger(RailML2Data.class);

	/**
	 * Erstellt die kompletten Fahrplandaten aus der Fahrplan-XML (am angegebenen Pfad).
	 * @param path (Pfad)
	 * @return Fahrplandaten
	 * @throws IOException
	 * @throws JAXBException
	 */
	public static TimetableData loadML(String path) throws IOException, JAXBException {
		int countStations = 0;
		int countTrainCategories = 0;
		int countTrainRoutes = 0;
		int countStops = 0;

		TimetableData data = new TimetableData();

		RailMLParser parser = new RailMLParser();

		LOGGER.info("Parsing " + path);
		Railml railml = parser.parseRailML(path);
		LOGGER.info("Parsed " + path);

		Infrastructure infra = railml.getInfrastructure();
		for (EOcp ocp : infra.getOperationControlPoints().getOcp()) {
			TOcpOperationalType ocptype = ocp.getPropOperational().getOperationalType();
			if (ocptype == TOcpOperationalType.STATION || ocptype == null) { //Entweder Bahnhof oder unbestimmt (da manche Halte unbestimmt sind!)
				data.addStation(new Station(ocp.getId(), ocp.getName()));
				countStations += 1;
			}
		}


		Timetable timetable = railml.getTimetable();
		for (ECategory cat : timetable.getCategories().getCategory()) {
			//Categories auslesen

			//Achtung! UsageType kann null sein --> aufpassen bei usageType.name()!
			TUsageType usageType = cat.getTrainUsage();
			String trainUsage = "";
			if (usageType != null) {
				trainUsage = usageType.name();
			}

			String catId = cat.getId();
			String catName = cat.getName();
			String catDesc = cat.getDescription();
			if (catId == null) catId = "";
			if (catName == null) catName = "";
			if (catDesc == null) catDesc = "";

			data.addTrainCategory(new TrainCategory(catId, catName, catDesc, trainUsage));
			countTrainCategories += 1;
		}

		for (ETrainPart trainPart : timetable.getTrainParts().getTrainPart()) {
			List<Stop> stops = new ArrayList<Stop>();

			for (EOcpTT ocptt : trainPart.getOcpsTT().getOcpTT()) {
				String ocpttID = ((EOcp) ocptt.getOcpRef()).getId();
				Station station = data.getStationById(ocpttID);

				if (station != null && !ocptt.getOcpType().equals("pass")) { //auch hier gibts Stopsignale etc; zudem sollen Passes sollen nicht angezeigt werden
					StopType stopType;

					//nur zur Initialisierung
					LocalTime arrival = null;
					LocalTime departure = null;

					String type = ocptt.getOcpType();
					switch (type) {
						case "begin":
							stopType = StopType.BEGIN;
							break;
						case "pass":
							stopType = StopType.PASS;
							break;
						case "stop":
							stopType = StopType.STOP;
							break;
						default:
							stopType = StopType.END;
					}


					if (stopType == StopType.STOP || stopType == StopType.END) {
						XMLGregorianCalendar calArrival = ocptt.getTimes().get(0).getArrival();
						arrival = LocalTime.of(calArrival.getHour(), calArrival.getMinute(), calArrival.getSecond());
					}

					if (stopType != StopType.END) {
						XMLGregorianCalendar calDeparture = ocptt.getTimes().get(0).getDeparture();
						departure = LocalTime.of(calDeparture.getHour(), calDeparture.getMinute(), calDeparture.getSecond());
					}


					String track = "";
					if (ocptt.getTrackInfo() != null) {
						track = ocptt.getTrackInfo();
					} else {
						track = "";
					}

					Stop stop = new Stop(station, stopType, arrival, departure, track, 0);

					stops.add(stop);
					countStops += 1;
				}
			}

			int trainNumber = Integer.parseInt(trainPart.getTrainNumber());

			if (stops.size() > 0) {
				data.addTrainRoute(new TrainRoute(trainPart.getId(), Integer.toString(trainNumber),
						data.getTrainCategoryById(((ECategory) trainPart.getCategoryRef()).getId()), stops, 0));
				countTrainRoutes += 1;
			}
		}
		LOGGER.info("Successfully loaded RailML!"
				+ countTrainCategories + " TrainCategories, "
				+ countStations + " Stations, "
				+ countTrainRoutes + " TrainRoutes, "
				+ countStops + " Stops."
		);
		return data;
	}
}
