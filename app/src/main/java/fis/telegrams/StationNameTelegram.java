package fis.telegrams;

/**
 * Eine Klasse für Bahnhofsnamentelegramme. 
 * @author spiollinux, kloppstock
 */
public class StationNameTelegram extends Telegram{
	private byte ID;
	private String code;
	private String name;
	
	/**
	 * Konstruktor für Bahnhofsnamentelegramme. 
	 * @param ID
	 * @param code
	 * @param name
	 * @throws NullPointerException
	 */
	public StationNameTelegram(byte ID, String code, String name) throws NullPointerException {
		if(code == null || name == null)
			throw new NullPointerException();
		this.ID = ID;
		this.code = code;
		this.name = name;
	}
	
	/**
	 * Getter für ID. 
	 * @return ID
	 */
	public byte getID(){
		return this.ID;
	}
	
	/**
	 * Getter für code.
	 * @return code
	 */
	public String getCode(){
		return this.code;
	}
	
	/**
	 * Getter für name.
	 * @return name
	 */
	public String getName(){
		return this.name;
	}
}
