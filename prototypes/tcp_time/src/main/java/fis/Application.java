package fis;

import fis.telegrams.TelegramParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;

/**
 * Created by spiollinux on 05.11.15.
 */
@EnableAsync
@SpringBootApplication
public class Application {

    @Autowired TelegramParser parser;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("---------running-----------");
    }

    @PostConstruct
    void initialize() {
        parser.start();
    }
}
