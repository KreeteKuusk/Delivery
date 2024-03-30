package org.example.delivery.controller;

import org.example.delivery.utilities.DeliveryFeeCalculator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class DeliveryController {

    private final DeliveryFeeCalculator deliveryFeeCalculator;

    public DeliveryController(DeliveryFeeCalculator deliveryFeeCalculator) {
        this.deliveryFeeCalculator = deliveryFeeCalculator;
    }

    @GetMapping("/calculateDeliveryFee") // GET request example -> /calculateDeliveryFee?city=Tallinn&vehicleType=car
    public ResponseEntity<Double> calculateDeliveryFee(@RequestParam String cityName, @RequestParam String vehicleType) {
        // Calculating the total delivery fee
        double totalDeliveryFee = deliveryFeeCalculator.calculateTotalDeliveryFee(cityName, vehicleType);
        return new ResponseEntity<>(totalDeliveryFee, HttpStatus.OK);


    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
