package com.example.readingservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ReadingController {

    @Autowired
    CircuitBreakerFactory circuitBreakerFactory;



    @GetMapping("/to-read")
    public String toRead(){
        return circuitBreakerFactory.create("recommended").run(
                () ->  new RestTemplate().getForObject("http://localhost:8090/recommended", String.class),
                throwable -> "Cloud Native Java (O'Reilly)"
        );
    }
}
