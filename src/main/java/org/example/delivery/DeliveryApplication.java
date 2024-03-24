package org.example.delivery;

import org.example.delivery.utilities.XMLDataFetcher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
        XMLDataFetcher fetcher = new XMLDataFetcher();
        try {
            fetcher.fetchDataFromURL("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php");
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

}
