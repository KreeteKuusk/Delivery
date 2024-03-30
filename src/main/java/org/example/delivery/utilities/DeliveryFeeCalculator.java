package org.example.delivery.utilities;

import org.example.delivery.model.Weather;
import org.example.delivery.repository.WeatherRepository;
import org.springframework.stereotype.Service;

@Service
public class DeliveryFeeCalculator {

    private final WeatherRepository weatherRepository;

    public DeliveryFeeCalculator(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    /**
     * Method for calculating the total delivery fee
     *
     * @param cityName    name of the city for calculating the regional base fee and getting weather data
     * @param vehicleType type of vehicle for calculating the regional base fee and weather conditions extra fee
     * @return the total delivery fee
     */
    public double calculateTotalDeliveryFee(String cityName, String vehicleType) {
        // Converting to lowercase
        String city = cityName.toLowerCase();
        String vehicle = vehicleType.toLowerCase();

        // Total Delivery Fee = Regional Base fee + Weather Conditions Extra Fee
        double rbf = calculateRegionalBaseFee(city, vehicle); // Calculating regional base fee
        double ef = calculateExtraFee(city, vehicle); // Calculating weather conditions extra fee
        return rbf + ef;
    }

    /**
     * Method for calculating the regional base fee
     *
     * @param city    name of the city
     * @param vehicle type of vehicle
     * @return regional base fee
     */
    private double calculateRegionalBaseFee(String city, String vehicle) {
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

    /**
     * Method for calculating the extra fee based on weather conditions and vehicle type
     *
     * @param city    name of the city
     * @param vehicle type of vehicle
     * @return total extra fee
     */
    private double calculateExtraFee(String city, String vehicle) {
        // Getting the latest weather data. If the data is present then assign it to weather variable
        // We also need the stationName, not the city name to fetch data
        Weather weather = weatherRepository.findTopCityOrderByTimestampDesc(getStationNameFromCityName(city))
                .orElseThrow(() -> new IllegalArgumentException("No weather data available for city: " + city));

        // Air temperature extra fee
        double atef = calculateAirTemperatureExtraFee(weather.getTemperature(), vehicle);
        // Weather phenomenon extra fee
        double wpef = calculateWeatherPhenomenonExtraFee(weather.getPhenomenon(), vehicle);
        // Wind speed extra fee
        double wsef = calculateWindSpeedExtraFee(weather.getWindSpeed(), vehicle);

        return atef + wpef + wsef;
    }

    /**
     * Method for getting the station name based on city name
     *
     * @param city city name
     * @return station for this city
     */
    private String getStationNameFromCityName(String city) {
        if (city.equals("tallinn")) return "Tallinn-Harku";
        else if (city.equals("tartu")) return "Tartu-Tõravere";
        else if (city.equals("pärnu")) return "Pärnu";
        else throw new IllegalArgumentException("Station name not provided for this city");
    }

    /**
     * Method for calculating the extra fee based on wind speed
     *
     * @param wspeed  wind speed
     * @param vehicle type of vehicle
     * @return total extra fee
     */
    private double calculateWindSpeedExtraFee(double wspeed, String vehicle) {
        if (vehicle.equals("bike")) {
            if (10 <= wspeed && wspeed <= 20) return 0.5;
            else if (wspeed > 20) throw new IllegalArgumentException("Usage of selected vehicle type is forbidden");
        }
        return 0;
    }

    /**
     * Method for calculating the extra fee based on weather phenomenon
     *
     * @param wp      weather phenomenon
     * @param vehicle type of vehicle
     * @return total extra fee
     */
    private double calculateWeatherPhenomenonExtraFee(String wp, String vehicle) {
        if (wp == null) return 0; // If there is no weather phenomenon, then there's no extra fee
        if (vehicle.equals("bike") || vehicle.equals("scooter")) {
            if (wp.contains("snow") || wp.contains("fleet")) return 1;
            else if (wp.contains("rain")) return 0.5;
            else if (wp.equals("glaze") || wp.equals("hail") || wp.equals("thunder")) {
                throw new IllegalArgumentException("Usage of selected vehicle type is forbidden");
            }
        }
        return 0;
    }

    /**
     * Method for calculating the extra fee based on air temperature
     *
     * @param temp    air temperature
     * @param vehicle type of vehicle
     * @return total extra fee
     */
    private double calculateAirTemperatureExtraFee(double temp, String vehicle) {
        if (vehicle.equals("bike") || vehicle.equals("scooter")) {
            if (-10 <= temp && temp <= 0) return 0.5;
            else if (temp < -10) return 1;
        }
        return 0;
    }

}