package fis.telegrams;

import java.time.LocalTime;

/**
 * @author spiollinux, kloppstock
 * Eine Klasse für Laborzeittelegramme.
 */
public class LabTimeTelegram extends Telegram {
	private LocalTime time;
	
	/**
	 * Konstruktor für das Laborzeittelegramm. 
	 * @param time (Zeit)
	 * @throws NullPointerException
	 */
	public LabTimeTelegram(LocalTime time) throws NullPointerException {
		if(time == null)
			throw new NullPointerException();
		this.time = time;
	}
	
	/**
	 * Getter für time.
	 * @return	time (Zeit)
	 */
	public LocalTime getTime(){
		return this.time;
	}

	public boolean equals(LabTimeTelegram telegram) {
		return this.getTime().equals(telegram.getTime());
	}
}
