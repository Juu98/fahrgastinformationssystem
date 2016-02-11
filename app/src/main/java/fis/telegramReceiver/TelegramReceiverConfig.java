/* Copyright 2016 Erik Schölzel, Robert Mörseburg, Zdravko Yanakiev, Jonas Schenke, Oliver Schmidt
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
package fis.telegramReceiver;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Enthält Konfigurationsdaten für den TelegramReceiver. Wird von Spring über
 * {@link org.springframework.boot.context.properties.ConfigurationProperties}
 * automatisch mit Daten aus der Konfigurationsdatei befüllt
 */
@Component
@ConfigurationProperties(prefix = "telegramserver")
public class TelegramReceiverConfig {
	private String hostname;
	private int port;
	private byte clientID;
	private int timeout = 1000;
	private int timeTillReconnect = 5000;

	public int getTimeTillReconnect() {
		return timeTillReconnect;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public byte getClientID() {
		return clientID;
	}

	public void setClientID(byte clientID) {
		this.clientID = clientID;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
