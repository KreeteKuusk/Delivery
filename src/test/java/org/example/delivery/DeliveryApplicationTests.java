package org.example.delivery;

import org.example.delivery.controller.DeliveryController;
import org.example.delivery.model.Weather;
import org.example.delivery.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * This class is for testing
 */

@SpringBootTest
class DeliveryApplicationTests {

    @Autowired
    private DeliveryController deliveryContr;

    @MockBean
    private WeatherRepository weatherRepo;

    @Test
    void contextLoads() {
        // empty test that would fail if our Spring configuration does not load correctly
    }

    @Test
    void testCalculateDeliveryFeeValid1() {
        // Arrange
        String cityName = "TARTU";
        String stationName = "Tartu-Tõravere";
        String vehicleType = "BIKE";

        Weather weather = new Weather();
        weather.setTemperature((float) -2.1); // should add 0.5€
        weather.setWindSpeed((float) 4.7); // should add 0€
        weather.setPhenomenon("Light snow shower"); // should add 1€
        double expectedDeliveryFee = 4.0; // Tartu + bike should add 2.5€

        // When findCityOrder... method is called then it returns the new weather made before
        // We also need to call the sql query with the correct station name
        when(weatherRepo.findTopCityOrderByTimestampDesc(stationName))
                .thenReturn(Optional.of(weather));

        // Act
        ResponseEntity<Double> response = deliveryContr.calculateDeliveryFee(cityName, vehicleType);

        // Assert
        assertEquals(expectedDeliveryFee, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCalculateDeliveryFeeValid2() {
        // Arrange
        String cityName = "PÄRNU";
        String stationName = "Pärnu";
        String vehicleType = "CAR";

        Weather weather = new Weather();
        weather.setTemperature((float) 12); // should add 0€
        weather.setWindSpeed((float) 3); // should add 0€
        weather.setPhenomenon("Light fleet"); // should add 0€
        double expectedDeliveryFee = 3; // Pärnu + car should add 3€

        // When findCityOrder... method is called then it returns the new weather made before
        // We also need to call the sql query with the correct station name
        when(weatherRepo.findTopCityOrderByTimestampDesc(stationName))
                .thenReturn(Optional.of(weather));

        // Act
        ResponseEntity<Double> response = deliveryContr.calculateDeliveryFee(cityName, vehicleType);

        // Assert
        assertEquals(expectedDeliveryFee, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCalculateDeliveryFeeValid3() {
        // Arrange
        String cityName = "TALLINN";
        String stationName = "Tallinn-Harku";
        String vehicleType = "SCOOTER";

        Weather weather = new Weather();
        weather.setTemperature((float) 4); // should add 0€
        weather.setWindSpeed((float) 12); // should add 0€
        double expectedDeliveryFee = 3.5; // Tallinn + scooter should add 3.5€

        // When findCityOrder... method is called then it returns the new weather made before
        // We also need to call the sql query with the correct station name
        when(weatherRepo.findTopCityOrderByTimestampDesc(stationName))
                .thenReturn(Optional.of(weather));

        // Act
        ResponseEntity<Double> response = deliveryContr.calculateDeliveryFee(cityName, vehicleType);

        // Assert
        assertEquals(expectedDeliveryFee, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCalculateDeliveryFeeValid4() {
        // Arrange
        String cityName = "TALLINN";
        String stationName = "Tallinn-Harku";
        String vehicleType = "CAR";

        Weather weather = new Weather();
        weather.setTemperature((float) 26); // should add 0€
        weather.setWindSpeed((float) 1); // should add 0€
        double expectedDeliveryFee = 4; // Tallinn + car should add 4€

        // When findCityOrder... method is called then it returns the new weather made before
        // We also need to call the sql query with the correct station name
        when(weatherRepo.findTopCityOrderByTimestampDesc(stationName))
                .thenReturn(Optional.of(weather));

        // Act
        ResponseEntity<Double> response = deliveryContr.calculateDeliveryFee(cityName, vehicleType);

        // Assert
        assertEquals(expectedDeliveryFee, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testForbiddenWindSpeed() {
        // Arrange
        String cityName = "PÄRNU";
        String stationName = "Pärnu";
        String vehicleType = "BIKE";

        Weather weather = new Weather();
        weather.setTemperature((float) 4); // should add 0€
        weather.setPhenomenon("rain"); // should add 0.5€
        weather.setWindSpeed((float) 34); // should throw error
        String expectedMessage = "Usage of selected vehicle type is forbidden";

        // When findCityOrder... method is called then it returns the new weather made before
        // We also need to call the sql query with the correct station name
        when(weatherRepo.findTopCityOrderByTimestampDesc(stationName))
                .thenReturn(Optional.of(weather));

        // Act and Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> deliveryContr.calculateDeliveryFee(cityName, vehicleType));
        String actualMessage = exception.getMessage();

        // Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testForbiddenPhenomenon() {
        // Arrange
        String cityName = "TARTU";
        String stationName = "Tartu-Tõravere";
        String vehicleType = "SCOOTER";

        Weather weather = new Weather();
        weather.setTemperature((float) 4); // should add 0€
        weather.setWindSpeed((float) 3); // should add 0€
        weather.setPhenomenon("thunder"); // should throw error
        String expectedMessage = "Usage of selected vehicle type is forbidden";

        // When findCityOrder... method is called then it returns the new weather made before
        // We also need to call the sql query with the correct station name
        when(weatherRepo.findTopCityOrderByTimestampDesc(stationName))
                .thenReturn(Optional.of(weather));

        // Act and Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> deliveryContr.calculateDeliveryFee(cityName, vehicleType));
        String actualMessage = exception.getMessage();

        // Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testInvalidVehicleType() {
        // Arrange
        String cityName = "TARTU";
        String vehicleType = "SKATEBOARD";

        String expectedMessage = "Invalid vehicle type";

        // Act and Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> deliveryContr.calculateDeliveryFee(cityName, vehicleType));
        String actualMessage = exception.getMessage();

        // Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testInvalidCityName() {
        // Arrange
        String cityName = "HELSINGI";
        String vehicleType = "BIKE";

        String expectedMessage = "Invalid city name";

        // Act and Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> deliveryContr.calculateDeliveryFee(cityName, vehicleType));
        String actualMessage = exception.getMessage();

        // Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
