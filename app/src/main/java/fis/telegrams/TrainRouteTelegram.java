package fis.telegrams;

import fis.data.TrainRoute;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Eine Klasse für Zuglauftelegramme. 
 * @author spiollinux, kloppstock
 */
public class TrainRouteTelegram extends Telegram {
	public static class TrainRouteData {
		private final String trainNumber;
		private final String trainCategoryShort;
		private final int messageId;
		private final List<StopData> stopDataList;

		public TrainRouteData(String trainNumber, String trainCategoryShort, int messageId, List<StopData> stopDataList) {
			this.trainNumber = trainNumber;
			this.trainCategoryShort = trainCategoryShort;
			this.messageId = messageId;
			this.stopDataList = stopDataList;
		}

		/* auto-generated */
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final TrainRouteData other = (TrainRouteData) obj;
			if (!Objects.equals(this.trainNumber, other.trainNumber)) {
				return false;
			}
			if (!Objects.equals(this.trainCategoryShort, other.trainCategoryShort)) {
				return false;
			}
			if (this.messageId != other.messageId) {
				return false;
			}
			if (!Objects.equals(this.stopDataList, other.stopDataList)) {
				return false;
			}
			return true;
		}

		/* auto-generated */
		@Override
		public String toString() {
			return "TrainRouteData{" + "trainNumber=" + trainNumber + ", trainCategoryShort=" + trainCategoryShort + ", messageId=" + messageId + ", stopDataList=" + stopDataList + '}';
		}
		
	}
	
	public static class StopData {
		private final int stationId;
		private final LocalTime scheduledArrival;
		private final LocalTime scheduledDeparture;
		private final LocalTime actualArrival;
		private final LocalTime actualDeparture;
		private final int scheduledTrack;
		private final int actualTrack;
		private final int dispoType;
		private final int messageId;

		public StopData(int stationId, LocalTime scheduledArrival, LocalTime scheduledDeparture, LocalTime actualArrival, LocalTime actualDeparture, int scheduledTrack, int actualTrack, int dispoType, int messageId) {
			this.stationId = stationId;
			this.scheduledArrival = scheduledArrival;
			this.scheduledDeparture = scheduledDeparture;
			this.actualArrival = actualArrival;
			this.actualDeparture = actualDeparture;
			this.scheduledTrack = scheduledTrack;
			this.actualTrack = actualTrack;
			this.dispoType = dispoType;
			this.messageId = messageId;
		}

		@Override
		/* auto-generated */
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final StopData other = (StopData) obj;
			if (this.stationId != other.stationId) {
				return false;
			}
			if (!Objects.equals(this.scheduledArrival, other.scheduledArrival)) {
				return false;
			}
			if (!Objects.equals(this.scheduledDeparture, other.scheduledDeparture)) {
				return false;
			}
			if (!Objects.equals(this.actualArrival, other.actualArrival)) {
				return false;
			}
			if (!Objects.equals(this.actualDeparture, other.actualDeparture)) {
				return false;
			}
			if (this.scheduledTrack != other.scheduledTrack) {
				return false;
			}
			if (this.actualTrack != other.actualTrack) {
				return false;
			}
			if (this.dispoType != other.dispoType) {
				return false;
			}
			if (this.messageId != other.messageId) {
				return false;
			}
			return true;
		}

		/* auto-generated */
		@Override
		public String toString() {
			return "StopData{" + "stationId=" + stationId + ", scheduledArrival=" + scheduledArrival + ", scheduledDeparture=" + scheduledDeparture + ", actualArrival=" + actualArrival + ", actualDeparture=" + actualDeparture + ", scheduledTrack=" + scheduledTrack + ", actualTrack=" + actualTrack + ", dispoType=" + dispoType + ", messageId=" + messageId + '}';
		}
		
	}
	
	private TrainRoute route;
	private TrainRouteData trainRouteData;
	
	/**
	 * Konstruktor für Zuglauftelegramme.
	 * @param route
	 * @throws NullPointerException
	 */
	public TrainRouteTelegram(TrainRoute route) throws NullPointerException {
		if(route == null)
			throw new NullPointerException();
		this.route = route;
	}
	
	public TrainRouteTelegram(TrainRouteData trainRouteData){
		if (trainRouteData == null){
			throw new IllegalArgumentException("versuch ein TrainRouteTelegramm ohne Daten anzulegen!");
		}
		this.trainRouteData = trainRouteData;
	}
	
	/**
	 * Funktion zum Anhängen eines Zuglaufes an den in der Klasse vorhandenen. 
	 * @param route
	 * @throws NullPointerException
	 */
	public void appendRoute(TrainRoute route) throws NullPointerException {
		if(route == null)
			throw new NullPointerException();
		
		//Aufpassen wegen der Datenkonsistenz -> TrainRoute.addStops benutzen!
		this.route.addStops(route.getStops());
	}
	
	/**
	 * Getter für route. 
	 * @return route
	 */
	public TrainRoute getTrainRoute(){
		return this.route;
	}
	
	public TrainRouteData getData(){
		return trainRouteData;
	}

	@Override
	public String toString() {
		if (this.route == null){
			return String.format("TrainRouteTelegram [DATA]: %s", this.trainRouteData);
		}
		return String.format("TrainRouteTelegram [ROUTE]: %s", this.route);
	}

	@Override
	public boolean equals(Object other) {
		if (!other.getClass().equals(this.getClass())){
			return false;
		}
		TrainRouteTelegram o = (TrainRouteTelegram) other;
		
		if (this.route == null) return this.trainRouteData.equals(o.getData());		
		return this.route.equals(o.getTrainRoute());
	}
}
