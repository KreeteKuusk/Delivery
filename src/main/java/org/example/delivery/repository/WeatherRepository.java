package org.example.delivery.repository;

import org.example.delivery.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * This interface provides methods for interacting with the database
 */

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Integer> {
    /**
     * Method for finding the latest weather data for a specific city
     *
     * @param city name of the city
     * @return the latest weather object
     */
    @Query(value = "SELECT w FROM Weather w WHERE w.stationName = :city ORDER BY w.timestamp DESC LIMIT 1")
    Optional<Weather> findTopCityOrderByTimestampDesc(@Param("city") String city);
}
