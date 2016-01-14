package fis.common;

/**
 * Exception, die Probleme in der Konfiguration der Anwendung aufzeigen soll
 */
public class ConfigurationException extends Exception {
	public ConfigurationException() {
		super();
	}

	public ConfigurationException(String message) {
		super(message);
	}
}