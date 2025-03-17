package com.sangto.rental_car_server;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
public class RentalCarServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalCarServerApplication.class, args);
        System.out.println("http://localhost:8080/swagger-ui/index.html");
    }

}
