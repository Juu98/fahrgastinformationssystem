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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package fis;

import fis.common.CommonConfig;
import fis.common.EventTranslator;
import fis.data.TimetableController;
import fis.telegramReceiver.TelegramReceiverController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import javax.annotation.PostConstruct;

/**
 * Hauptklasse der Anwendung. Startet und initialisiert die Anwendung.
 */
@SuppressWarnings("FieldCanBeLocal")
@SpringBootApplication
@EnableAsync
public class Application implements ApplicationEventPublisherAware {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	private ApplicationEventPublisher publisher;

	@Bean
	public Java8TimeDialect timeDialect() {
		return new Java8TimeDialect();
	}

	/**
	 * initialisiert Anwendung nach dem Start.
	 * Prüft, ob nach gewisser Zeit Verbindung zum Server besteht
	 */
	@PostConstruct
	private void initialize() {

	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}
}
