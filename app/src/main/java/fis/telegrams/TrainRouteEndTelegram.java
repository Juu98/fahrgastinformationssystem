package fis.telegrams;

/**
 * Eine Klasse für Zuglaufendtelegramme. 
 * @author spiollinux, kloppstock
 */
public class TrainRouteEndTelegram extends Telegram{
	
	/**
	 * Konstruktor für Zuglaufendtelegramme. 
	 */
	public TrainRouteEndTelegram(){
		
	}

	@Override
	public String toString() {
		return "TrainRouteEndTelegram";
	}

	@Override
	public boolean equals(Object other) {
		// only check if it's another TrainRouteEndTelegram
		return this.getClass().equals(other.getClass());
	}
}
