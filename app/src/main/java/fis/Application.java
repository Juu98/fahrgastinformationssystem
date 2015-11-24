package fis;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fis.data.TimetableController;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

/**
 * Created by spiollinux on 05.11.15.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
	
	@Autowired TimetableController timetable;
	
	@Bean
	public Java8TimeDialect timeDialect(){
		return new Java8TimeDialect();
	}
}
