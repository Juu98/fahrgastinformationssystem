package fis.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Enthält allgemeine, nicht komponentenspezifische Konfigurationswerte.
 * Wird automatisch von Spring mit Werten aus Configfiles befüllt.
 */
@Component
@ConfigurationProperties(prefix = "fis")
public class CommonConfig {
	private int telegramserverTimeout;

	public int getTelegramserverTimeout() {
		return telegramserverTimeout;
	}

	public void setTelegramServerTimeout(int telegramServerTimeout) {
		this.telegramserverTimeout = telegramServerTimeout;
	}
}
