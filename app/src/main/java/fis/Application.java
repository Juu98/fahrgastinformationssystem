package fis;

import fis.common.CommonConfig;
import fis.common.ConfigurationException;
import fis.data.TimetableEvent;
import fis.data.TimetableEventType;
import fis.telegramReceiver.ConnectionStatusEvent;
import fis.telegramReceiver.ConnectionStatus;
import fis.telegramReceiver.TelegramReceiverController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fis.data.TimetableController;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import javax.annotation.PostConstruct;

/**
 * Hauptklasse der Anwendung. Startet und initialisiert die Anwendung.
 */
@SpringBootApplication
@EnableAsync
public class Application implements ApplicationEventPublisherAware{
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

	private Logger LOGGER = Logger.getLogger(Application.class);
	private ApplicationEventPublisher publisher;
	@Autowired private TimetableController timetable;
	@Autowired private TelegramReceiverController receiverController;
	@Autowired private CommonConfig commonConfig;
	
	@Bean
	public Java8TimeDialect timeDialect(){
		return new Java8TimeDialect();
	}

	/**
	 * initialisiert Anwendung nach dem Start.
	 * Prüft, ob nach gewisser Zeit Verbindung zum Server besteht
	 */
	@PostConstruct
	private void initialize() {
		try {
			Thread.sleep(commonConfig.getTelegramserverTimeout());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		if(receiverController.getConnectionStatus() != ConnectionStatus.ONLINE) {
			publisher.publishEvent(new TimetableEvent(TimetableEventType.parseRailML));
		}
	}

	/**
	 * Übersetzt ConnectionStatusEvents in TimetableEvents
	 * @param event
	 */
	@EventListener
	public void notifyReceiverOffline(ConnectionStatusEvent event) {
		ConnectionStatus receivedStatus = event.getStatus();
		if (receivedStatus.equals(ConnectionStatus.OFFLINE)) {
			publisher.publishEvent(new TimetableEvent(TimetableEventType.parseRailML));
		}
		else if (receivedStatus.equals(ConnectionStatus.ONLINE)) {
			publisher.publishEvent(new TimetableEvent(TimetableEventType.cleanup));
		}
	}


	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}
}
