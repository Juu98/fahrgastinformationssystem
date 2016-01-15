package fis.web;

import fis.Application;
import fis.FilterTime;
import fis.FilterType;
import fis.data.Station;
import fis.data.TimetableController;
import fis.data.TrainCategory;
import fis.data.TrainRoute;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Haupt-Controller-Klasse der Anwendung.
 * <p>
 * <p>
 * Ist für Darstellung der GUI verantwortlich und verarbeitet sämtliche
 * Nutzereingaben.
 * <p>
 * <p>
 * Bietet außerdem die REST-Schnittstelle mit einigen Daten im JSON-Format.
 *
 * @author Robert Mörseburg (<a href="https://github.com/fl4m">Github</a>)
 */
@Controller
public class FisController {
	private final TimetableController timetable;
	private static final Logger LOGGER = Logger.getLogger(FisController.class);

	/**
	 * Verwendungszweck der anzuzeigenden Zugläufe.
	 */
	private static final String TRAIN_USAGE = "PASSENGER";

	/**
	 * Standardkonstruktor.
	 * <p>
	 * <p>
	 * Der {@link TimetableController} wird durch die Spring-Annotation
	 * automatisch angelegt und übergeben.
	 *
	 * @param tt Der aktuell gültige {@link TimetableController}
	 */
	@Autowired
	public FisController(TimetableController tt) {
		this.timetable = tt;
	}

	/**
	 * Fügt Standardparameter zum Model hinzu.
	 * <p>
	 * <p>
	 * Es werden die aktuelle Zeit sowie der Verbindungsstatus bei jedem
	 * Seitenaufruf dem {@link Model} hinzugefügt.
	 *
	 * @param model das Model der {@link Application}
	 */
	public void defaults(Model model) {
		// aktuelle Laborzeit
		model.addAttribute("time", this.timetable.getTime());

		// Verbindungsstatus zum Fahrplanserver
		model.addAttribute("connectionState", this.timetable.getStateName());

		// Meldungen
		model.addAttribute("messageMap", this.timetable.getMessageMap());

		// Version der Anwendung
		// wird nur bei direkter Ausführung der jar geladen
		String v = Application.class.getPackage().getImplementationVersion();
		if (v == null) {
			LOGGER.error("Couldn't retrieve version information. Maybe you didn't execute the jar?");
			v = "XX.Y.Z (jar ausführen!)";
		}
		model.addAttribute("programVersion", v);
	}

	/**
	 * Startseite der Anwendung.
	 * <p>
	 * <p>
	 * Wird beim Aufruf von
	 * <a href="http://localhost:8080">http://localhost:8080</a> aufgerufen und
	 * leitet an die Abfahrtsanzeige weiter.
	 *
	 * @return Spring-redirect
	 */
	@RequestMapping({"/", "/fis"})
	public String index() {
		return "redirect:/dep/";
	}

	/**
	 * Startseite der Abfahrtsanzeige.
	 * <p>
	 * <p>
	 * Wird angezeigt, wenn noch kein Bahnhof ausgewählt wurde.
	 *
	 * @param model das Model der {@link Application}
	 * @param form  Nutzereingaben im {@link FilterForm}
	 * @return leere Abfahrtsanzeige durch Verarbeitung des
	 * <a href="/src/main/resources/templates/dep.html">
	 * Abfahrtstemplates</a>
	 */
	@RequestMapping("/dep/")
	public String depDefault(Model model, FilterForm form) {
		return dep(model, form, null);
	}

	/**
	 * Weiterleitung bei inkorrekter URI.
	 * <p>
	 * <p>
	 * Es werden vom System nur URIs mit "trailing slash" verwendet. Sollte
	 * dieser fehlen, so wird er durch diese Methode angehängt.
	 * <p>
	 * <p>
	 * Der Slash ist wichtig, damit die (relativen) Links im Template
	 * ordnungsgemäß funktionieren.
	 *
	 * @return Spring-redirect
	 */
	@RequestMapping("/dep")
	public String depRedir() {
		return "redirect:/dep/";
	}

	/**
	 * Verarbeitungsmethode der Abfahrtsanzeige.
	 * <p>
	 * <p>
	 * Der URL-Parameter {@code \{stn\}} enthält die ID der aktuell ausgewählten
	 * {@link Station}.
	 * <p>
	 * <p>
	 * Diese Methode ermittelt alle anzuzeigenden Zugläufe und stellt diese
	 * sowei einen {@link TrainRouteComparator} zum Sortieren im {@link Model}
	 * bereit.
	 *
	 * @param model das Model der {@link Application}
	 * @param form  Nutzereingaben im {@link FilterForm}
	 * @param stn   ID der aktuellen {@link Station}
	 * @return Abfahrtsanzeige durch Verarbeitung des
	 * <a href="/src/main/resources/templates/dep.html">
	 * Abfahrtstemplates</a>
	 */
	@RequestMapping("/dep/{stn}")
	public String dep(Model model, FilterForm form, @PathVariable("stn") String stn) {
		// Standardparameter zum Model hinzufügen
		defaults(model);

		// Formularzustand bestimmen
		boolean formSent = (form.getSubmit() != null && !form.getSubmit().isEmpty());
		boolean resetForm = (form.getReset() != null && !form.getReset().isEmpty());

		// aktuelle Station bestimmen
		Station currentStation = null;
		if (stn != null && !stn.isEmpty()) {
			currentStation = this.timetable.getData().getStationById(stn);
		}
		if (form.getStationId() != null) {
			currentStation = this.timetable.getData().getStationById(form.getStationId());
		}
		LOGGER.debug("*DEP* Current station: " + currentStation);

		// aktuell anzuzeigende Zugtypen bestimmen
		List<TrainCategory> currentCategories;
		if (!formSent || resetForm) {
			currentCategories = this.timetable.getTrainCategories(TRAIN_USAGE);
		} else {
			currentCategories = new ArrayList<>();
			if (form.getCategories() != null) {
				for (String tc : form.getCategories()) {
					currentCategories.add(this.timetable.getTrainCategoryById(tc));
				}
			}
		}
		model.addAttribute("currentCategories", currentCategories);

		// Zeitraum
		LocalTime start = null, end = null;
		if (formSent && !resetForm) {
			try {
				start = LocalTime.parse(form.getStart());
				end = LocalTime.parse(form.getEnd());
			} catch (DateTimeParseException e) {
				// LOGGER.error(e);
				start = null;
				end = null;
			}
		}

		if (start == null || end == null) {
			start = this.timetable.getTime();
			// nicht verbunden
			if (start == null){
				start = LocalTime.now();
			}
			end = start.plus(2, ChronoUnit.HOURS);
		}

		// Zeitraum zum Model hinzufügen
		model.addAttribute("start", start);
		model.addAttribute("end", end);

		// Comparator zum Sortieren zum Model hinzufügen
		model.addAttribute("comp",
				new TrainRouteComparator(currentStation, FilterTime.SCHEDULED, FilterType.DEPARTURE));

		// alle Zugläufe mit diesem Bahnhof zum Model hinzufügen
		// sofern sie im Zeitrahmen liegen
		model.addAttribute("trains",
				this.filter(currentStation, FilterType.DEPARTURE, start, end, FilterTime.SCHEDULED));
		// this.timetable.getTrainRoutesByStation(currentStation,FilterType.DEPARTURE),
		// currentStation,
		// start, end,
		// FilterType.DEPARTURE, FilterTime.SCHEDULED
		// ));
		model.addAttribute("form", form);

		// alle Bahnhöfe für die Liste zum Model hinzufügen
		model.addAttribute("stations", this.timetable.getData().getStations());
		model.addAttribute("currentStation", currentStation);

		// alle Zugkategorien zum Model hinzufügen
		model.addAttribute("categories", this.timetable.getTrainCategories(TRAIN_USAGE));

		return "dep";
	}

	/**
	 * Startseite der Ankunftsanzeige.
	 * <p>
	 * <p>
	 * Wird angezeigt, wenn noch kein Bahnhof ausgewählt wurde.
	 *
	 * @param model das Model der {@link Application}
	 * @param form  Nutzereingaben im {@link FilterForm}
	 * @return leere Ankunftsanzeige durch Verarbeitung des
	 * <a href="/src/main/resources/templates/arr.html">
	 * Ankunftstemplates</a>
	 */
	@RequestMapping("/arr/")
	public String arrDefault(Model model, FilterForm form) {
		return arr(model, form, null);
	}

	/**
	 * Weiterleitung bei inkorrekter URI.
	 * <p>
	 * <p>
	 * Es werden vom System nur URIs mit "trailing slash" verwendet. Sollte
	 * dieser fehlen, so wird er durch diese Methode angehängt.
	 * <p>
	 * <p>
	 * Der Slash ist wichtig, damit die (relativen) Links im Template
	 * ordnungsgemäß funktionieren.
	 *
	 * @return Spring-redirect
	 */
	@RequestMapping("/arr")
	public String arrRedir() {
		return "redirect:/arr/";
	}

	/**
	 * Verarbeitungsmethode der Ankunftsanzeige.
	 * <p>
	 * <p>
	 * Der URL-Parameter {@code \{stn\}} enthält die ID der aktuell ausgewählten
	 * {@link Station}.
	 * <p>
	 * <p>
	 * Diese Methode ermittelt alle anzuzeigenden zugläufe und stellt diese
	 * sowei einen {@link TrainRouteComparator} zum Sortieren im {@link Model}
	 * bereit.
	 *
	 * @param model das Model der {@link Application}
	 * @param form  Nutzereingaben im {@link FilterForm}
	 * @param stn   ID der aktuellen {@link Station}
	 * @return Ankunftsanzeige durch Verarbeitung des
	 * <a href="/src/main/resources/templates/arr.html">
	 * Ankunftstemplates</a>
	 */
	@RequestMapping("/arr/{stn}")
	public String arr(Model model, FilterForm form, @PathVariable("stn") String stn) {
		// Standardparameter zum Model hinzufügen
		defaults(model);

		// Formularzustand bestimmen
		boolean formSent = (form.getSubmit() != null && !form.getSubmit().isEmpty());
		boolean resetForm = (form.getReset() != null && !form.getReset().isEmpty());

		// aktuelle Station bestimmen
		Station currentStation = null;
		if (stn != null && !stn.isEmpty()) {
			currentStation = this.timetable.getData().getStationById(stn);
		}
		if (form.getStationId() != null) {
			currentStation = this.timetable.getData().getStationById(form.getStationId());
		}
		LOGGER.debug("*ARR* Current station: " + currentStation);

		// aktuell anzuzeigende Zugtypen bestimmen
		List<TrainCategory> currentCategories;
		if (!formSent || resetForm) {
			currentCategories = this.timetable.getTrainCategories(TRAIN_USAGE);
		} else {
			currentCategories = new ArrayList<>();
			if (form.getCategories() != null) {
				for (String tc : form.getCategories()) {
					currentCategories.add(this.timetable.getTrainCategoryById(tc));
				}
			}
		}
		model.addAttribute("currentCategories", currentCategories);

		// Zeitraum
		LocalTime start = null, end = null;
		if (formSent && !resetForm) {
			try {
				start = LocalTime.parse(form.getStart());
				end = LocalTime.parse(form.getEnd());
			} catch (DateTimeParseException e) {
				// LOGGER.error(e);
				start = null;
				end = null;
			}
		}

		if (start == null || end == null) {
			start = this.timetable.getTime();
			// nicht verbunden
			if (start == null){
				start = LocalTime.now();
			}
			end = start.plus(2, ChronoUnit.HOURS);
		}

		// Zeitraum zum Model hinzufügen
		model.addAttribute("start", start);
		model.addAttribute("end", end);

		// Comparator zum Sortieren zum Model hinzufügen
		model.addAttribute("comp", new TrainRouteComparator(currentStation, FilterTime.SCHEDULED, FilterType.ARRIVAL));

		// Alle Zugläufe mit diesem Bahnhof zum Model hinzufügen
		// sofern sie im Zeitrahmen liegen
		model.addAttribute("trains", this.filter(currentStation, FilterType.ARRIVAL, start, end, FilterTime.SCHEDULED));
		// this.timetable.getTrainRoutesByStation(currentStation,FilterType.ARRIVAL),
		// currentStation,
		// start, end,
		// FilterType.ARRIVAL, FilterTime.SCHEDULED
		// ));
		model.addAttribute("form", form);

		// alle Bahnhöfe für die Liste zum Model hinzufügen
		model.addAttribute("stations", this.timetable.getData().getStations());
		model.addAttribute("currentStation", currentStation);

		// alle Zugkategorien zum Model hinzufügen
		model.addAttribute("categories", this.timetable.getTrainCategories(TRAIN_USAGE));

		return "arr";
	}

	/**
	 * Startseite der Zuglaufanzeige.
	 * <p>
	 * <p>
	 * Wird angezeigt, wenn noch kein Zuglauf ausgewählt wurde.
	 *
	 * @param model  das Model der {@link Application}
	 * @param formTR Nutzereingaben im {@link TrainRouteForm}
	 * @return leere Zuglaufanzeige durch Verarbeitung des
	 * <a href="/src/main/resources/templates/trn.html">
	 * Zuglaufstemplates</a>
	 */
	@RequestMapping("/trn/")
	public String trnDefault(Model model, TrainRouteForm formTR) {
		return trn(model, formTR, null);
	}

	/**
	 * Weiterleitung bei inkorrekter URI.
	 * <p>
	 * <p>
	 * Es werden vom System nur URIs mit "trailing slash" verwendet. Sollte
	 * dieser fehlen, so wird er durch diese Methode angehängt.
	 * <p>
	 * <p>
	 * Der Slash ist wichtig, damit die (relativen) Links im Template
	 * ordnungsgemäß funktionieren.
	 *
	 * @return Spring-redirect
	 */
	@RequestMapping("/trn")
	public String trnRedir() {
		return "redirect:/trn/";
	}

	@RequestMapping("/graph")
	public String graphRedir() {
		return "redirect:/graph/";
	}

	@RequestMapping("/graph/")
	public String defaultGraph(Model model) {
		return graph(model, null);
	}

	@RequestMapping("/graph/{stn}")
	public String graph(Model model, @PathVariable("stn") String stn) {
		// Standardparameter zum Model hinzufügen
		defaults(model);

		Station currentStation = null;

		if (stn != null) {
			currentStation = timetable.getData().getStationById(stn);
		}
		model.addAttribute("stations", timetable.getStations());
		model.addAttribute("trainRoutes", timetable.getTrainRoutes());
		model.addAttribute("currentStation", currentStation);
		return "graph";
	}

	/**
	 * Verarbeitungsmethode der Zuglaufanzeige.
	 * <p>
	 * <p>
	 * Der URL-Parameter {@code \{trt\}} enthält die ID der aktuellen
	 * {@link TrainRoute}.
	 * <p>
	 * <p>
	 * Die Methode ermittelt die darzustellende {@link TrainRoute} aus diesem
	 * Parameter oder dem Formular und fügt diese dem {@link Model} hinzu.
	 *
	 * @param model  das Model der {@link Application}
	 * @param formTR Nutzereingaben im {@link TrainRouteForm}
	 * @param trt    ID der aktuellen {@link TrainRoute}
	 * @return Zuglaufanzeige durch Verarbeitung des
	 * <a href="/src/main/resources/templates/trn.html">
	 * Zuglaufstemplates</a>
	 */
	@RequestMapping("/trn/{trt}")
	public String trn(Model model, TrainRouteForm formTR, @PathVariable("trt") String trt) {
		// Standardparameter zum Model hinzufügen
		defaults(model);

		// aktuelle TrainRoute bestimmen
		TrainRoute currentTrainRoute = null;
		if (trt != null && !trt.isEmpty()) {
			currentTrainRoute = this.timetable.getData().getTrainRouteById(trt);
		}
		if (formTR.getTrainRouteId() != null) {
			currentTrainRoute = this.timetable.getData().getTrainRouteById(formTR.getTrainRouteId());
		}
		LOGGER.debug("*TRN* Current train route: " + currentTrainRoute);

		// TrainRoutes zum Model hinzufügen
		model.addAttribute("trainRoutes", this.timetable.getData().getTrainRoutes());
		model.addAttribute("currentTrainRoute", currentTrainRoute);

		return "trn";
	}

	/**
	 * Liefert eine JSON-Liste mit allen {@link Station}s in diesem Fahrplan.
	 * <p>
	 * <p>
	 * Wird asynchron abgerufen und liefert die Daten für die Textersetzung im
	 * {@code typeahead}-Element der Bahnhofssuche.
	 * <p>
	 * <p>
	 * Die Daten werden durch den {@link JSONProvider} so gefiltert, dass für
	 * jeden Bahnhof nur ID und Name übergeben werden.
	 *
	 * @return JSON body
	 */
	@RequestMapping("stations.json")
	public @ResponseBody List<JSONProvider.StationView> getStations() {
		return new JSONProvider().getStations(this.timetable.getData().getStations());
	}


	/**
	 * Liefert eine JSON-Liste mit allen {@link TrainRoute}s in diesem Fahrplan.
	 * <p>
	 * <p>
	 * Wird asynchron abgerufen und liefert die Daten für die Textersetzung im
	 * {@code typeahead}-Element der Zuglaufsuche.
	 * <p>
	 * <p>
	 * Die Daten werden durch den {@link JSONProvider} so gefiltert, dass für
	 * jeden Zuglauf nur ID, Zugnummer, Anfang und Ende aufgeführt werden.
	 *
	 * @return JSON body
	 * @see JSONProvider.TrainRouteView
	 */
	@RequestMapping("trainRoutes.json")
	public
	@ResponseBody
	List<JSONProvider.TrainRouteView> getTrainRoutes() {
		return new JSONProvider().getTrainRoutes(this.timetable.getData().getTrainRoutes());
	}

	@RequestMapping("fullTrainRoutes.json")
	public 	@ResponseBody List<JSONProvider.FullTrainRouteView> getFullTrainRoutes() {
		return new JSONProvider().getFullTrainRoutes(this.timetable.getData().getTrainRoutes());
	}


	/**
	 * Liefert eine JSON-Liste mit allen {@link TrainCategory} Einträgen des
	 * aktuellen Fahrplans.
	 * <p>
	 * <p>
	 * Wird für die Ausgabe der Liste der Zugtypen zum Filtern verwendet.
	 *
	 * @return JSON body
	 */
	@RequestMapping("trainCategories.json")
	public
	@ResponseBody
	List<TrainCategory> getTrainCategories() {
		return this.timetable.getTrainCategories();
	}

	/**
	 * Methode zur Filterung von ZUgläufen
	 *
	 * @param station
	 * @param type       (Abfahrt / Ankunft)
	 * @param from       (Startzeit)
	 * @param to         (Endzeit)
	 * @param filterTime (geplant / tatsächlich)
	 * @return Eine Liste der gefilterten Zugläufe
	 * @throws IllegalArgumentException
	 * @throws UnsupportedOperationException
	 */
	public List<TrainRoute> filter(Station station, FilterType type, LocalTime from, LocalTime to,
	                               FilterTime filterTime) throws IllegalArgumentException, UnsupportedOperationException {
		if (type == null || from == null || to == null || filterTime == null)
			throw new IllegalArgumentException();

		if (type.equals(FilterType.ANY))
			throw new UnsupportedOperationException();

		if (station == null) {
			return new ArrayList<TrainRoute>();
		}

		Iterable<TrainRoute> routes = this.timetable.getTrainRoutesByStation(station, type);
		List<TrainRoute> filtered = new ArrayList<TrainRoute>();
		for (TrainRoute route : routes) {
			if (route.getStopAtStation(station) == null) {
				LOGGER.debug("No stations for " + route + " at " + station);
				continue;
			}
			if (type.equals(FilterType.ARRIVAL)) {
				if (filterTime == FilterTime.ACTUAL) {
					if (route.getStopAtStation(station).getActualArrival() != null) {
						if (route.getStopAtStation(station).getActualArrival().isAfter(from)
								&& route.getStopAtStation(station).getActualArrival().isBefore(to)) {
							filtered.add(route);
						}
					} else
						LOGGER.debug("keine actual departure auf " + route + " für " + station);
				} else {
					if (route.getStopAtStation(station).getScheduledArrival() != null) {
						if (route.getStopAtStation(station).getScheduledArrival().isAfter(from)
								&& route.getStopAtStation(station).getScheduledArrival().isBefore(to)) {
							filtered.add(route);
						}
					} else {
						LOGGER.debug("keine scheduled arrival auf " + route + " für " + station);
					}
				}
			} else if (type.equals(FilterType.DEPARTURE)) {
				if (filterTime == FilterTime.ACTUAL) {
					if (route.getStopAtStation(station).getActualDeparture() != null) {
						if (route.getStopAtStation(station).getActualDeparture().isAfter(from)
								&& route.getStopAtStation(station).getActualDeparture().isBefore(to)) {
							filtered.add(route);
						}
					} else {
						LOGGER.debug("keine actual departure auf " + route + " für " + station);
					}
				} else {
					if (route.getStopAtStation(station).getScheduledDeparture() != null) {
						if (route.getStopAtStation(station).getScheduledDeparture().isAfter(from)
								&& route.getStopAtStation(station).getScheduledDeparture().isBefore(to)) {
							filtered.add(route);
						}
					} else {
						LOGGER.debug("keine scheduled departure auf " + route + " für " + station);
					}
				}
			}
		}
		return filtered;
	}

}
