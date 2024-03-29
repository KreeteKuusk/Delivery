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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
        weather.setTemperature((float)-2.1); // should add 0.5€
        weather.setWindSpeed((float)4.7); // should add 0€
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

}
