package fis.telegrams;

/**
 * Eine Klasse für Bahnhofsnamentelegramme.
 *
 * @author schmittlauch, kloppstock
 */
public class StationNameTelegram extends Telegram {
	private byte id;
	private String code;
	private String name;
	private float x;
	private float y;

	/**
	 * Konstruktor für Bahnhofsnamentelegramme.
	 *
	 * @param ID
	 * @param code
	 * @param name
	 * @throws IllegalArgumentException
	 */
	public StationNameTelegram(byte ID, String code, String name, float x, float y) throws IllegalArgumentException {
		if (code == null || name == null)
			throw new IllegalArgumentException("Name und Abkürzung dürfen nicht null sein.");
		this.id = ID;
		this.code = code;
		this.name = name;
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter für ID.
	 *
	 * @return ID
	 */
	public byte getId() {
		return this.id;
	}

	/**
	 * Getter für code.
	 *
	 * @return code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * Getter für name.
	 *
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}


	@Override
	public String toString() {
		return String.format("StationNameTelegram: ID %0#4x; [%s] %s", this.id, this.code, this.name);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!other.getClass().equals(this.getClass())) {
			return false;
		}
		StationNameTelegram o = (StationNameTelegram) other;
		return
				this.id == o.getId() &&
						this.code.equals(o.getCode()) &&
						this.name.equals(o.getName());
	}
}
