package org.example.delivery.repository;

import org.example.delivery.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Integer> {

    // SQL query for finding the latest weather data
    @Query(value = "SELECT w FROM Weather w WHERE w.stationName = :city ORDER BY w.timestamp DESC LIMIT 1")
    Optional<Weather> findTopCityOrderByTimestampDesc(@Param("city") String city);
}
