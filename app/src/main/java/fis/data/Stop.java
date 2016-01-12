package fis.data;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Repräsentiert einen spezifischen Halt
 * 
 * @author Eric
 *
 */
public class Stop {
    private Station station;
    private LocalTime scheduledDeparture;
    private LocalTime scheduledArrival;
    private LocalTime actualArrival;
    private LocalTime actualDeparture;

    private String scheduledTrack;
    private String actualTrack;
    private int messageId; // Hier bin ich noch unsicher, was dort genau rein
			   // soll
    private StopType stopType;
    private TrainRoute trainRoute;

    /**
     * Erzeugt einen neuen Halt
     * 
     * @param station
     *            Der Bahnhof, an dem der Zug hält
     * @param stopType
     *            {@link StopType} dieses Haltes
     * @param scheduledArrival
     *            Geplante Ankunftszeit des Zuges
     * @param scheduledDeparture
     *            Geplante Abfahrtszeit des Zuges.
     * @param scheduledTrack
     *            Geplanter Gleis, an dem der Zug halten soll
     */
    public Stop(Station station, StopType stopType, LocalTime scheduledArrival, LocalTime scheduledDeparture,
	    String scheduledTrack, int messageId) {
	if (station == null || stopType == null)
	    throw new IllegalArgumentException();
	this.station = station;
	this.stopType = stopType;
	this.scheduledArrival = scheduledArrival;
	this.scheduledDeparture = scheduledDeparture;
	this.scheduledTrack = scheduledTrack;

	this.actualArrival = scheduledArrival;
	this.actualDeparture = scheduledDeparture;
	this.actualTrack = scheduledTrack;

	this.messageId = messageId;

	station.addStop(this);
    }

    /**
     * Getter für station
     * 
     * @return Den Bahnhof {@link Station}, an dem dieser Halt erfolgt.
     */
    public Station getStation() {
	return station;
    }

    /**
     * Setzt den Link auf den Zuglauf {@link TrainRoute}, zu der dieser Halt
     * gehört.
     * 
     * @param route
     *            Der Zuglauf {@link TrainRoute}, der diesen Halt beinhaltet.
     */
    public void setTrainRoute(TrainRoute route) {
	this.trainRoute = route;
    }

    /**
     * Entfernt die Verlinkung dieses Halts zum bisherigen Zuglauf
     * {@link TrainRoute} Aufrufen, wenn dieser Halt entfernt werden soll (für
     * Datenkonsistenz). Wird im Normalfall automatisch durch
     * {@link Station#removeStop(Stop)} oder {@link TrainRoute#removeStops()}
     * ausgeführt.
     */
    public void deleteStop() {
	if (trainRoute != null) {
	    trainRoute.getStops().remove(this);
	}

	if (station != null) {
	    station.getStops().remove(this);
	}

    }

    /**
     * Getter für stopType
     * 
     * @return {@link StopType} dieses Haltes
     */
    public StopType getStopType() {
	return stopType;
    }

    /**
     * Aktualisiert den {@link StopType}
     * 
     * @param newStopType
     *            Der neue {@link StopType}
     */
    public void updateStopType(StopType newStopType) {
	if (newStopType == null)
	    throw new IllegalArgumentException("newStopType darf nicht null sein");
	this.stopType = newStopType;
    }

    /**
     * Aktualisiert die tatsächliche Ankunftszeit des Zuges
     * 
     * @param actualArrival
     *            Die neue tatsächliche Ankunftszeit.
     */
    public void updateArrival(LocalTime actualArrival) {
	// if(actualArrival==null) throw new
	// IllegalArgumentException("actualArrival darf nicht null sein");
	this.actualArrival = actualArrival;
    }

    /**
     * Aktualisiert die tatsächliche Abfahrtszeit des Zuges
     * 
     * @param actualDeparture
     *            Die neue tatsächliche Abfahrtszeit
     */
    public void updateDeparture(LocalTime actualDeparture) {
	// if(actualDeparture==null) throw new
	// IllegalArgumentException("actualDeparture darf nicht null sein");
	this.actualDeparture = actualDeparture;
    }

    /**
     * Aktualisiert den tatsächlichen Gleis, an de, der Zug hält.
     * 
     * @param actualTrack
     *            Der neue Gleis.
     */
    public void updateTrack(String actualTrack) {
	if (actualTrack == null)
	    throw new IllegalArgumentException("actualTrack darf nicht null sein");
	this.actualTrack = actualTrack;
    }

    /**
     * Aktualisiert die aktuelle Meldung
     * 
     * @param messageId
     */
    public void updateMessage(int messageId) {
	this.messageId = messageId;
    }

    /**
     * @return Die Id der Message
     */
    public int getMessageId() {
	return messageId;
    }

    /**
     * @return Geplante Abfahrtszeit
     */
    public LocalTime getScheduledDeparture() {
	return scheduledDeparture;
    }

    /**
     * @return Geplante Ankunftszeit
     */
    public LocalTime getScheduledArrival() {
	return scheduledArrival;
    }

    /**
     * @return Tatsächliche Abfahrtszeit
     */
    public LocalTime getActualDeparture() {
	return actualDeparture;
    }

    /**
     * @return Tatsächliche Ankunftszeit
     */
    public LocalTime getActualArrival() {
	return actualArrival;
    }

    /**
     * @return Geplanter Gleis
     */
    public String getScheduledTrack() {
	return scheduledTrack;
    }

    /**
     * @return Tatsächlicher Gleis
     */
    public String getActualTrack() {
	return actualTrack;
    }

    /**
     * @return Der Zuglauf {@link TrainRoute}, der diesen Halt beinhaltet (muss
     *         natürlich vorher gesetzt worden sein!)
     */
    public TrainRoute getTrainRoute() {
	return trainRoute;
    }

	/**Berechnet die Verspätung bei der Ankunft in Sekunden. 
	 * @return Positives Ergebnis, wenn der Zug zu spät ankommt
	 * Negatives Ergebnis, wenn der Zug zu früh ankommt.
	 */
	public long getDelayArrival(){
		if(actualArrival.isBefore(scheduledArrival)){
			return -this.actualArrival.until(this.scheduledArrival, ChronoUnit.SECONDS);
		}
		else{
			return this.scheduledArrival.until(this.actualArrival,ChronoUnit.SECONDS);	
		}
	}
	/**Berechnet die Verspätung bei der Abfahrt in Sekunden. 
	 * @return Positives Ergebnis, wenn der Zug zu spät abfährt
	 * Negatives Ergebnis, wenn der Zug zu früh abfährt.
	 */
	public long getDelayDeparture(){
		if(actualDeparture.isBefore(scheduledDeparture)){
			return -this.actualDeparture.until(this.scheduledDeparture, ChronoUnit.SECONDS);
		}
		else{
			return this.scheduledDeparture.until(this.actualDeparture,ChronoUnit.SECONDS);	
		}
	}
	
	/**
	 * @return Liefert die Ankunftsverspätung in der Form [-]MM.SS
	 */
	public String getDelayArrivalString(){
		long delay = this.getDelayArrival();
		String str = "";
		
		if(delay < 0){
			LocalTime time = LocalTime.MIDNIGHT.plus(0 - delay, ChronoUnit.SECONDS);
			String min = Integer.toString(time.getMinute());
			if(min.length()==1){
				min = "0"+min;
			}
			String sec = Integer.toString(time.getSecond());
			if(sec.length()==1){
				sec = "0"+sec;
			}
			str = "-" + min + ":" + sec;
		} else if (delay > 0) {
			LocalTime time = LocalTime.MIDNIGHT.plus(delay, ChronoUnit.SECONDS);
			String min = Integer.toString(time.getMinute());
			if(min.length()==1){
				min = "0"+min;
			}
			String sec = Integer.toString(time.getSecond());
			if(sec.length()==1){
				sec = "0"+sec;
			}
			str = "+" + min + ":" + sec;
		}
		return str;
	}
	
	/**
	 * @return Liefert die Abfahrtsverspätung in der Form [-]MM.SS
	 */
	public String getDelayDepartureString(){
		long delay = this.getDelayDeparture();
		String str = "";
		
		if(delay < 0){
			LocalTime time = LocalTime.MIDNIGHT.plus(0 - delay, ChronoUnit.SECONDS);
			String min = Integer.toString(time.getMinute());
			if(min.length()==1){
				min = "0"+min;
			}
			String sec = Integer.toString(time.getSecond());
			if(sec.length()==1){
				sec = "0"+sec;
			}
			str = "-" + min + ":" + sec;
		} else if (delay > 0) {
			LocalTime time = LocalTime.MIDNIGHT.plus(delay, ChronoUnit.SECONDS);
			String min = Integer.toString(time.getMinute());
			if(min.length()==1){
				min = "0"+min;
			}
			String sec = Integer.toString(time.getSecond());
			if(sec.length()==1){
				sec = "0"+sec;
			}
			str = "+" +min + ":" + sec;
		}
		return str;
	}
	
	
}
