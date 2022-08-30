package com.gsdai.matchingsys;

import com.gsdai.matchingsys.service.impl.MatchingServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchingSysApplication {
    public static void main(String[] args) {
        MatchingServiceImpl.matchPool.start();
        SpringApplication.run(MatchingSysApplication.class, args);
    }
}
