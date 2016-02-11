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
package fis.telegramReceiver;

import org.springframework.context.ApplicationEvent;

/**
 * Event, das auf eine Änderung des Verbindungsstatus des TelegramReceiverControllers zum Server hinweist
 */
public class ConnectionStatusEvent extends ApplicationEvent {
	private ConnectionStatus status;

	/**
	 * Erstellt ein neues ConnectionStatusEvent als Kapselung für einen {@link ConnectionStatus}
	 *
	 * @param status im Event zu sendender ConnectionStatus
	 */
	public ConnectionStatusEvent(ConnectionStatus status) {
		super(status);
		this.status = status;
	}

	/**
	 * gibt den Status in das Event gekapselten ConnectionStatus zurück
	 *
	 * @return ConnectionStatus
	 */
	public ConnectionStatus getStatus() {
		return status;
	}
}
