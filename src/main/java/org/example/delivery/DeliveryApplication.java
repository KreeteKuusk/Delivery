package org.example.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DeliveryApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DeliveryApplication.class, args);
    }

}
