package com.prototypes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:Beans.xml")
public class Prototype1Application {

    public static void main(String[] args) {
        SpringApplication.run(Prototype1Application.class, args);
    }
    
}
