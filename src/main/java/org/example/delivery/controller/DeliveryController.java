package org.example.delivery.controller;

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

            // Total Delivery Fee = Regional Base fee + Weather Conditions Extra Fee
            double rbf = deliveryFeeCalculator.calculateRegionalBaseFee(cityName, vehicleType); // Calculating regional base fee
            double ef = deliveryFeeCalculator.calculateExtraFee(cityName, vehicleType); // Calculating weather conditions extra fee
            return new ResponseEntity<>(rbf + ef, HttpStatus.OK);


    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
