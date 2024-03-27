package org.example.delivery.controller;

import org.example.delivery.repository.WeatherRepository;
import org.example.delivery.utilities.DeliveryFeeCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class DeliveryController {

    @Autowired
    private DeliveryFeeCalculator deliveryFeeCalculator;

    @GetMapping("/calculateDeliveryFee") // GET request example -> /calculateDeliveryFee?city=Tallinn&vehicleType=car
    public ResponseEntity<Double> calculateDeliveryFee(@RequestParam String cityName, @RequestParam String vehicleType) {
        // total delivery fee = regional base fee +  weather conditions extra fee
        double totalFee = 0;
        try {
            totalFee += deliveryFeeCalculator.calculateRegionalBaseFee(cityName, vehicleType); // Calculating regional base fee
            totalFee += deliveryFeeCalculator.calculateExtraFee(cityName, vehicleType); // Calculating weather conditions extra fee
            return new ResponseEntity<>(totalFee, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
