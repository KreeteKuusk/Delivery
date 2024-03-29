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
            throw new IllegalArgumentException("Invalid city name");
        }

        return rbf;
    }

    public double calculateExtraFee(String cityName, String vehicleType) {
        // Converting to lowercase
        String vehicle = vehicleType.toLowerCase();
        String city = cityName.toLowerCase();

        // Getting the latest weather data. If the data is present then assign it to weather variable
        // We also need the stationName, not the cityname to fetch data
        Weather weather = weatherRepository.findTopCityOrderByTimestampDesc(getStationNameFromCityName(city))
                .orElseThrow(() -> new IllegalArgumentException("No weather data available for city: " + cityName));

        // Air temperature extra fee
        double atef = calculateAirTemperatureExtraFee(weather.getTemperature(), vehicle);
        // Weather phenomenon extra fee
        double wpef = calculateWeatherPhenomenonExtraFee(weather.getPhenomenon(), vehicle);
        // Wind speed extra fee
        double wsef = calculateWindSpeedExtraFee(weather.getWindSpeed(), vehicle);

        return atef + wpef + wsef;
    }

    private String getStationNameFromCityName(String city) {
        if (city.equals("tallinn")) return  "Tallinn-Harku";
        else if (city.equals("tartu")) return  "Tartu-Tõravere";
        else if (city.equals("pärnu")) return  "Pärnu";
        else throw new IllegalArgumentException("Station name not provided for this city");
    }

    private double calculateWindSpeedExtraFee(double wspeed, String vehicle) {
        if (vehicle.equals("bike")) {
            if (10 <= wspeed && wspeed <= 20) return 0.5;
            else if (wspeed > 20) throw new IllegalArgumentException("Usage of selected vehicle type is forbidden");
        }
        return 0;
    }

    private double calculateWeatherPhenomenonExtraFee(String wp, String vehicle) {
        if (wp == null) return 0; // If there is no weather phenomenon, then there's no fee
        if (vehicle.equals("bike") || vehicle.equals("scooter")) {
            if (wp.contains("snow") || wp.contains("fleet")) return 1;
            else if (wp.contains("rain")) return 0.5;
            else if (wp.equals("glaze") || wp.equals("hail") || wp.equals("thunder")) {
                throw new IllegalArgumentException("Usage of selected vehicle type is forbidden");
            }
        }
        return 0;
    }

    private double calculateAirTemperatureExtraFee(double temp, String vehicle) {
        if (vehicle.equals("bike") || vehicle.equals("scooter")) {
            if (-10 <= temp && temp <= 0) return 0.5;
            else if (temp < -10) return 1;
        }
        return 0;
    }

}