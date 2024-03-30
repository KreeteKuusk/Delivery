package org.example.delivery.controller;

import org.example.delivery.utilities.DeliveryFeeCalculator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class is a REST controller for handling delivery related requests.
 */

@RestController
public class DeliveryController {

    private final DeliveryFeeCalculator deliveryFeeCalculator;

    public DeliveryController(DeliveryFeeCalculator deliveryFeeCalculator) {
        this.deliveryFeeCalculator = deliveryFeeCalculator;
    }

    /**
     * Calculates the total delivery fee based on the city name and vehicle type
     *
     * @param cityName    name of the city where the delivery is taking place
     * @param vehicleType type of vehicle used for delivery
     * @return a ResponseEntity containing the total delivery fee and HTTP status
     */
    @GetMapping("/calculateDeliveryFee") // GET request example -> /calculateDeliveryFee?city=Tallinn&vehicleType=car
    public ResponseEntity<Double> calculateDeliveryFee(@RequestParam String cityName, @RequestParam String vehicleType) {
        // Calculating the total delivery fee
        double totalDeliveryFee = deliveryFeeCalculator.calculateTotalDeliveryFee(cityName, vehicleType);
        return new ResponseEntity<>(totalDeliveryFee, HttpStatus.OK);


    }

    /**
     * Handles IllegalArgumentExceptions thrown by the calculateDeliveryFee method
     *
     * @param e the exception that was thrown
     * @return a ResponseEntity containing the error message and HTTP status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
