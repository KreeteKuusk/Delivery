package org.example.delivery.utilities;

import org.example.delivery.model.Weather;
import org.example.delivery.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryFeeCalculator {

    @Autowired
    private WeatherRepository weatherRepository;

    public double calculateRegionalBaseFee(String cityName, String vehicleType) {
        // Converting to lowercase
        String city = cityName.toLowerCase();
        String vehicle = vehicleType.toLowerCase();
        double rbf;

        // Checking city and vehicle type to calculate regional base fee
        if (city.equals("tallinn")) {
            rbf = switch (vehicle) {
                case "car" -> 4;
                case "scooter" -> 3.5;
                case "bike" -> 3;
                default -> throw new IllegalArgumentException("Invalid vehicle type");
            };

        } else if (city.equals("tartu")) {
            rbf = switch (vehicle) {
                case "car" -> 3.5;
                case "scooter" -> 3;
                case "bike" -> 2.5;
                default -> throw new IllegalArgumentException("Invalid vehicle type");
            };

        } else if (city.equals("pärnu")) {
            rbf = switch (vehicle) {
                case "car" -> 3;
                case "scooter" -> 2.5;
                case "bike" -> 2;
                default -> throw new IllegalArgumentException("Invalid vehicle type");
            };

        } else {
            throw new IllegalArgumentException("Invalid city type");
        }

        return rbf;
    }

    public double calculateExtraFee(String cityName, String vehicleType) {
        double extrafee = 0;

        // Converting to lowercase
        String vehicle = vehicleType.toLowerCase();
        String city = cityName.toLowerCase();

        // Getting the latest weather data
        String stationName;
        if (city.equals("tallinn")) stationName = "Tallinn-Harku";
        else if (city.equals("tartu")) stationName = "Tartu-Tõravere";
        else if (city.equals("pärnu")) stationName = "Pärnu";
        else throw new IllegalArgumentException("Station name not provided for this city");

        // Find the newest data from the list
        // If the data is present then assign it to weather variable
        Weather weather = weatherRepository.findTopByCityOrderByTimestampDesc(stationName)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No weather data available for city: " + cityName));

        if (vehicle.equals("bike") || vehicle.equals("scooter")) {
            // Air temperature
            float temp = weather.getTemperature();
            if (-10 <= temp && temp <= 0) extrafee += 0.5;
            else if (temp < -10) extrafee += 1;

            // Weather phenomenon
            String wp = weather.getPhenomenon();
            if (wp.contains("snow") || wp.contains("fleet")) extrafee += 1;
            else if (wp.contains("rain")) extrafee += 0.5;
            else if (wp.equals("glaze") || wp.equals("hail") || wp.equals("thunder")) {
                throw new IllegalArgumentException("Usage of selected vehicle type is forbidden");
            }
        }
        // Wind speed
        if (vehicle.equals("bike")) {
            float wspeed = weather.getWindSpeed();
            if (10 <= wspeed && wspeed <= 20) extrafee += 0.5;
            else if (wspeed > 20) throw new IllegalArgumentException("Usage of selected vehicle type is forbidden");
        }

        return extrafee;
    }

   /* private Weather getLatestWeatherData(String city) {
        String queryStr = "SELECT w FROM Weather w WHERE w.city = :city ORDER BY w.id DESC";
        TypedQuery<Weather> query = entityManager.createQuery(queryStr, Weather.class);
        query.setParameter("city", city);
        query.setMaxResults(1);
        return query.getSingleResult();
    }*/
}