package fis.telegrams;

import java.time.LocalTime;
import java.util.IllegalFormatCodePointException;

/**
 * @author spiollinux, kloppstock
 * Eine Klasse für Laborzeittelegramme.
 */
public class LabTimeTelegram extends Telegram {
	private LocalTime time;
	
	/**
	 * Konstruktor für das Laborzeittelegramm. 
	 * @param time (Zeit)
	 * @throws IllegalArgumentException
	 */
	public LabTimeTelegram(LocalTime time) throws IllegalArgumentException {
		if(time == null)
			throw new IllegalArgumentException();
		this.time = time;
	}
	
	/**
	 * Getter für time.
	 * @return	time (Zeit)
	 */
	public LocalTime getTime(){
		return this.time;
	}

	public boolean equals(Object other) {
		if (other == null){
			return false;
		}
		if (!other.getClass().equals(this.getClass())){
			return false;
		}
		
		LabTimeTelegram o = (LabTimeTelegram) other;
		return this.time.equals(o.getTime());
	}
	
	@Override
	public String toString(){
		return "Laborzeittelegramm: "+ this.time.toString();
	}
}
