package fis.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Enthält allgemeine, nicht komponentenspezifische Konfigurationswerte.
 * Wird automatisch von Spring mit Werten aus Configfiles (z. B. application.properties) befüllt.
 */
@Component
@ConfigurationProperties(prefix = "fis")
public class CommonConfig {
	private String railmlpath;
	private String logoPath;
	private String benutzertext;

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public String getBenutzertext() {
		return benutzertext;
	}

	public String getRailmlpath() {
		return railmlpath;
	}

	public void setRailmlpath(String railmlpath) {
		this.railmlpath = railmlpath;
	}

	public void setBenutzertext(String benutzertext) {
		this.benutzertext = benutzertext;
	}
}
