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

import fis.data.TimetableEvent;
import fis.data.TimetableEventType;
import fis.telegramReceiver.ConnectionStatus;
import fis.telegramReceiver.ConnectionStatusEvent;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Übersetzt Events von Komponenten in Events, die von anderen Komponenten verarbeitet werden können
 */
@Component
public class EventTranslator implements ApplicationEventPublisherAware {
	private ApplicationEventPublisher publisher;
	private boolean hasBeenOnlineBefore;
	private static final Logger LOGGER = Logger.getLogger(EventTranslator.class);

	public EventTranslator() {
		//needed for startup
		this.hasBeenOnlineBefore = true;
	}

	/**
	 * Übersetzt ConnectionStatusEvents in TimetableEvents
	 *
	 * @param event empfangenes ConnectionStatusEvent
	 */
	@EventListener
	public void translateReceiverToTimetable(ConnectionStatusEvent event) {
		ConnectionStatus receivedStatus = event.getStatus();
		if (receivedStatus.equals(ConnectionStatus.OFFLINE)) {
			if (this.hasBeenOnlineBefore) {
				publisher.publishEvent(new TimetableEvent(TimetableEventType.parseRailML));
				LOGGER.debug("published parseRailML event");
			}
			this.hasBeenOnlineBefore = false;
		} else if (receivedStatus.equals(ConnectionStatus.INIT)) {
			this.hasBeenOnlineBefore = true;
			publisher.publishEvent(new TimetableEvent(TimetableEventType.cleanup));
		}
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}
}
