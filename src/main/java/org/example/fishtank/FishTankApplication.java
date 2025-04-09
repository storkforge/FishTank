package org.example.fishtank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

// TEST COMMENT
@SpringBootApplication
@EnableCaching
public class FishTankApplication {
//
    public static void main(String[] args) {
        SpringApplication.run(FishTankApplication.class, args);
    }
}
