package fis;

import fis.telegrams.TelegramParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by spiollinux on 05.11.15.
 */
@SpringBootApplication
public class Application {

    @Autowired private TelegramParser parser;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("---------running-----------");
    }
}
