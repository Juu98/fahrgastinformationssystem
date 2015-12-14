package fis.telegrams;

/**
 * Eine Klasse für Bahnhofsnamentelegramme. 
 * @author spiollinux, kloppstock
 */
public class StationNameTelegram extends Telegram{
	private byte id;
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
		this.id = ID;
		this.code = code;
		this.name = name;
	}
	
	/**
	 * Getter für ID. 
	 * @return ID
	 */
	public byte getId(){
		return this.id;
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
	
	@Override
	public String toString(){
		return String.format("StationNameTelegram: ID %0#4x; [%s] %s", this.id, this.code, this.name);
	}
	
	@Override
	public boolean equals(Object other){
		if (!other.getClass().equals(this.getClass())){
			return false;
		}
		StationNameTelegram o = (StationNameTelegram) other;
		return
				this.id == o.getId() &&
				this.code.equals(o.getCode()) &&
				this.name.equals(o.getName());
	}
}
