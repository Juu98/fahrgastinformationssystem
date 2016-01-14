package fis.telegrams;

/**
 * Exception, die das Fehlschlagen des Parsens eines Rohtelegramms anzeigt
 */
public class TelegramParseException extends Exception {

	public TelegramParseException() {
		super();
	}

	public TelegramParseException(String message) {
		super(message);
	}
}
