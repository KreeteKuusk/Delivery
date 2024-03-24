package org.example.delivery.controller;

import org.example.delivery.model.Weather;
import org.example.delivery.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class WeatherController {
    @Autowired
    private WeatherRepository weatherRepository;

    @GetMapping("getAllWeather")
    public ResponseEntity<List<Weather>> getAllWeather() {
        try {
            List<Weather> weatherList = new ArrayList<>();
            // add all items to weatherList
            weatherList.addAll(weatherRepository.findAll());
            if (weatherList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            // return weatherList items if it's not empty
            return new ResponseEntity<>(weatherList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getWeatherById/{id}")
    public ResponseEntity<Weather> getWeatherById(@PathVariable Integer id) {
        try {
            Optional<Weather> weatherData = weatherRepository.findById(id);
            if (weatherData.isPresent()) {
                return new ResponseEntity<>(weatherData.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addWeather")
    public ResponseEntity<Weather> addWeather(@RequestBody Weather weather) {
        try {
            Weather weatherObject = weatherRepository.save(weather);
            return new ResponseEntity<>(weatherObject, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
