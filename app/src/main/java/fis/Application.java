package fis;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by spiollinux on 05.11.15.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
	
	@Autowired Timetable timetable;
	
	@PostConstruct
	void init(){
		timetable = new TimetableExample();
	}
}
