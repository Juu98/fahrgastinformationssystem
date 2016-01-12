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

		public String getTrainNumber() {
			return trainNumber;
		}

		public String getTrainCategoryShort() {
			return trainCategoryShort;
		}

		public int getMessageId() {
			return messageId;
		}

		public List<StopData> getStopDataList() {
			return stopDataList;
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

		public int getStationId() {
			return stationId;
		}

		public LocalTime getScheduledArrival() {
			return scheduledArrival;
		}

		public LocalTime getScheduledDeparture() {
			return scheduledDeparture;
		}

		public LocalTime getActualArrival() {
			return actualArrival;
		}

		public LocalTime getActualDeparture() {
			return actualDeparture;
		}

		public int getScheduledTrack() {
			return scheduledTrack;
		}

		public int getActualTrack() {
			return actualTrack;
		}

		public int getDispoType() {
			return dispoType;
		}

		public int getMessageId() {
			return messageId;
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

	public TrainRouteData getData() {
		return trainRouteData;
	}

	@Override
	public String toString() {
		return String.format("TrainRouteTelegram: %s", this.trainRouteData);
	}

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
