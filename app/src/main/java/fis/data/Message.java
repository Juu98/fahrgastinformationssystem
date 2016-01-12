package fis.data;

import java.io.Serializable;

/**
 * Eine Klasse für mögliche Nachrichten, die bei Stops angezeigt werden können.
 *
 * @author kloppstock
 */
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private int index;
	private String message;

	/**
	 * Getter für index.
	 *
	 * @return index
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * Getter für message.
	 *
	 * @return message (Nachricht)
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Setter für index.
	 *
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Setter für message.
	 *
	 * @param message (Nachricht)
	 * @throws NullPointerException
	 */
	public void setMessage(String message) throws NullPointerException {
		if (message == null)
			throw new NullPointerException();
		this.message = message;
	}

	@Override
	public String toString() {
		return this.getIndex() + ": " + ((this.getMessage() == null) ? "" : this.getMessage());
	}
}
