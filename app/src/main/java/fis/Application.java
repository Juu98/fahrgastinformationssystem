package fis;

import fis.telegramReceiver.TelegramReceiverController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fis.data.TimetableController;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import javax.annotation.PostConstruct;

/**
 * Created by spiollinux on 05.11.15.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
	
	@Autowired private TimetableController timetable;
	@Autowired private TelegramReceiverController receiverController;
	
	@Bean
	public Java8TimeDialect timeDialect(){
		return new Java8TimeDialect();
	}

	@PostConstruct
	private void initialize() {
		receiverController.start();
	}
}
