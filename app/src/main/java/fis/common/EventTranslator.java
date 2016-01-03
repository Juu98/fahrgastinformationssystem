package fis.common;

import fis.data.TimetableEvent;
import fis.data.TimetableEventType;
import fis.telegramReceiver.ConnectionStatus;
import fis.telegramReceiver.ConnectionStatusEvent;
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

	public EventTranslator() {
		this.hasBeenOnlineBefore = false;
	}

	/**
	 * Übersetzt ConnectionStatusEvents in TimetableEvents
	 * @param event
	 */
	@EventListener
	public void notifyReceiverOffline(ConnectionStatusEvent event) {
		ConnectionStatus receivedStatus = event.getStatus();
		if (receivedStatus.equals(ConnectionStatus.OFFLINE)) {
			if (this.hasBeenOnlineBefore) {
				publisher.publishEvent(new TimetableEvent(TimetableEventType.parseRailML));
			}
			this.hasBeenOnlineBefore = false;
		}
		else if (receivedStatus.equals(ConnectionStatus.ONLINE)) {
			this.hasBeenOnlineBefore = true;
			publisher.publishEvent(new TimetableEvent(TimetableEventType.cleanup));
		}
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}
}
