package com.mto.madetoorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class MadetoorderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MadetoorderApplication.class, args);
    }

}
