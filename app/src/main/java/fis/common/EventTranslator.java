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
	 * @param event
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
