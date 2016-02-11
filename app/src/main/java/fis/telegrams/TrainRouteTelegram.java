/* Copyright 2016 Eric Schölzel, Robert Mörseburg, Zdravko Yanakiev, Jonas Schenke, Oliver Schmidt
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
package fis.telegrams;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * Eine Klasse für Zuglauftelegramme.
 *
 * @author spiollinux, kloppstock
 */
public class TrainRouteTelegram extends Telegram {
	public static class TrainRouteData {
		private final String trainNumber;
		private final String trainCategoryShort;
		private final int messageId;
		private final List<StopData> stopDataList;

		/**
		 * Konstruktor für ein Zuglauftelegramm. 
		 * @param trainNumber
		 * @param trainCategoryShort
		 * @param messageId
		 * @param stopDataList
		 */
		public TrainRouteData(String trainNumber, String trainCategoryShort, int messageId, List<StopData> stopDataList) {
			this.trainNumber = trainNumber;
			this.trainCategoryShort = trainCategoryShort;
			this.messageId = messageId;
			this.stopDataList = stopDataList;
		}

		/**
		 * Testet ob ein anderes Objekt identisch zu dieser Instanz ist. 
		 * @param obj (anderes Objekt)
		 * @return {@literal true}, wenn sie gleich sind, sonst {@literal false}
		 */
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

		/**
		 * Konvertiert alle Daten der Klasse zu einem String.
		 * @return (String mit allen Daten)
		 */
		/* auto-generated */
		@Override
		public String toString() {
			return "TrainRouteData{" + "trainNumber=" + trainNumber + ", trainCategoryShort=" + trainCategoryShort + ", messageId=" + messageId + ", stopDataList=" + stopDataList + '}';
		}

		/**
		 * Getter für die Zugnummer. 
		 * @return trainNumber
		 */
		public String getTrainNumber() {
			return trainNumber;
		}
		
		/**
		 * Getter für die kurze Zuglaufkategorie. 
		 * @return trainCategoryShort
		 */
		public String getTrainCategoryShort() {
			return trainCategoryShort;
		}

		/**
		 * Getter für die Meldungstext ID. 
		 * @return trainNumber
		 */
		public int getMessageId() {
			return messageId;
		}

		/**
		 * Getter für die Liste aller Stopdaten. 
		 * @return stopDataList
		 */
		public List<StopData> getStopDataList() {
			return stopDataList;
		}
	}

	/**
	 * Eine für JSON-optimierte Stopdatenklasse.
	 * @author kloppstock
	 */
	public static class StopData {
		private final int stationId;
		private final LocalTime scheduledArrival;
		private final LocalTime scheduledDeparture;
		private final LocalTime actualArrival;
		private final LocalTime actualDeparture;
		private final boolean actualArrivalNextDay;
		private final boolean actualDepartureNextDay;
		private final int scheduledTrack;
		private final int actualTrack;
		private final int dispoType;
		private final int messageId;
		

		/**
		 * Konstruktor für die Stopdaten. 
		 * @param stationId
		 * @param scheduledArrival
		 * @param scheduledDeparture
		 * @param actualArrival
		 * @param actualDeparture
		 * @param scheduledTrack
		 * @param actualTrack
		 * @param dispoType
		 * @param messageId
		 */
		public StopData(int stationId, LocalTime scheduledArrival, LocalTime scheduledDeparture, LocalTime actualArrival,
		                LocalTime actualDeparture, int scheduledTrack, int actualTrack, int dispoType, int messageId,
		                boolean actualArrivalNextDay, boolean actualDepartureNextDay) {
			this.stationId = stationId;
			this.scheduledArrival = scheduledArrival;
			this.scheduledDeparture = scheduledDeparture;
			this.actualArrival = actualArrival;
			this.actualDeparture = actualDeparture;
			this.scheduledTrack = scheduledTrack;
			this.actualTrack = actualTrack;
			this.dispoType = dispoType;
			this.messageId = messageId;
			this.actualArrivalNextDay = actualArrivalNextDay;
			this.actualDepartureNextDay = actualDepartureNextDay;
		}

		/**
		 * Testet ob ein anderes Objekt identisch zu dieser Instanz ist. 
		 * @param obj (anderes Objekt)
		 * @return {@literal true}, wenn sie gleich sind, sonst {@literal false}
		 */
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

		/**
		 * Konvertiert alle Daten der Klasse zu einem String.
		 * @return (String mit allen Daten)
		 */
		/* auto-generated */
		@Override
		public String toString() {
			return "StopData{" + "stationId=" + stationId + ", scheduledArrival=" + scheduledArrival + ", scheduledDeparture=" + scheduledDeparture + ", actualArrival=" + actualArrival + ", actualDeparture=" + actualDeparture + ", scheduledTrack=" + scheduledTrack + ", actualTrack=" + actualTrack + ", dispoType=" + dispoType + ", messageId=" + messageId + '}';
		}

		/**
		 * Getter für die Stations ID.
		 * @return stationId
		 */
		public int getStationId() {
			return stationId;
		}

		/**
		 * Getter für die geplante Ankunftszeit.
		 * @return scheduledArrival
		 */
		public LocalTime getScheduledArrival() {
			return scheduledArrival;
		}

		/**
		 * Getter für die geplante Abfahrtszeit.
		 * @return scheduledDeparture
		 */
		public LocalTime getScheduledDeparture() {
			return scheduledDeparture;
		}
		
		/**
		 * Getter für die tatsächliche Ankunftszeit.
		 * @return actualArrival
		 */
		public LocalTime getActualArrival() {
			return actualArrival;
		}
		
		/**
		 * Getter für die tatsächliche Abfahrtszeit.
		 * @return actualDeparture
		 */
		public LocalTime getActualDeparture() {
			return actualDeparture;
		}

		/**
		 * Getter für den geplanten Bahnsteig. 
		 * @return scheduledTrack
		 */
		public int getScheduledTrack() {
			return scheduledTrack;
		}

		/**
		 * Getter für den tatsächlichen Bahnsteig. 
		 * @return actualTrack
		 */
		public int getActualTrack() {
			return actualTrack;
		}

		/**
		 * Getter für den Dispotypen. 
		 * @return dispoType
		 */
		public int getDispoType() {
			return dispoType;
		}

		/**
		 * Getter für die Meldungstext ID.
		 * @return messageId
		 */
		public int getMessageId() {
			return messageId;
		}
		
		/**
		 * @return Gibt an, ob die Verspätung der Ankunft auf den nächsten Tag zeigt
		 */
		public boolean getActualArrivalNextDay(){
			return this.actualArrivalNextDay;
		}
		
		/**
		 * @return Gibt an, ob die Verspätung der Abfahrt auf den nächsten Tag zeigt
		 */
		public boolean getActualDepartureNextDay(){
			return this.actualDepartureNextDay;
		}
	}

	private TrainRouteData trainRouteData;

	/**
	 * Konstruktor für Zuglauftelegramme.
	 *
	 * @param trainRouteData
	 */
	public TrainRouteTelegram(TrainRouteData trainRouteData) {
		if (trainRouteData == null) {
			throw new IllegalArgumentException("Versuch ein TrainRouteTelegramm ohne Daten anzulegen!");
		}
		this.trainRouteData = trainRouteData;
	}

	/**
	 * Getter für die Zuglaufdaten. 
	 * @return trainRouteData
	 */
	public TrainRouteData getData() {
		return trainRouteData;
	}

	/**
	 * Konvertiert alle Daten der Klasse zu einem String.
	 * @return (String mit allen Daten)
	 */
	@Override
	public String toString() {
		return String.format("TrainRouteTelegram: %s", this.trainRouteData);
	}

	/**
	 * Testet ob ein anderes Objekt identisch zu dieser Instanz ist. 
	 * @param obj (anderes Objekt)
	 * @return {@literal true}, wenn sie gleich sind, sonst {@literal false}
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!other.getClass().equals(this.getClass())) {
			return false;
		}
		TrainRouteTelegram o = (TrainRouteTelegram) other;

		return this.trainRouteData.equals(o.getData());
	}
}
