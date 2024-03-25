package org.example.delivery;

import org.example.delivery.utilities.XMLDataFetcher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DeliveryApplication {
    public static void main(String[] args) {
/*        SpringApplication.run(DeliveryApplication.class, args);
        XMLDataFetcher fetcher = new XMLDataFetcher();*/
        ConfigurableApplicationContext context = SpringApplication.run(DeliveryApplication.class, args);
        XMLDataFetcher fetcher = context.getBean(XMLDataFetcher.class);
        try {
            fetcher.fetchDataFromURL("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
