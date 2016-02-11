/* Copyright 2016 Eric Schölzel, Robert Mörseburg, Zdravko Yanakiev, Jonas Schenke, Oliver Schmidt
 *
 * This file is part of FIS.
 *
 * FIS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FIS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FIS.  If not, see <http://www.gnu.org/licenses/>.
 */
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
	private String messagecsvpath;
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

	public String getMessagecsvpath() {
		return messagecsvpath;
	}

	public void setMessagecsvpath(String messagecsvpath) {
		this.messagecsvpath = messagecsvpath;
	}
}
