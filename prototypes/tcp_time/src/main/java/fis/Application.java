package fis;

import fis.telegrams.TelegramReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * Created by spiollinux on 05.11.15.
 */
@EnableAsync
@SpringBootApplication
public class Application {

    @Autowired
    TelegramReceiver parser;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("---------running-----------");
    }

    @PostConstruct
    void initialize() {
        parser.start();
        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ConnectionStatus: " + parser.getConnectionStatus());
    }
}
