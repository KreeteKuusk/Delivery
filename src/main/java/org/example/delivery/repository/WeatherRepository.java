package org.example.delivery.repository;

import org.example.delivery.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Integer> {

    // For finding the latest weather data and storing it in a list (incase there's many with the same timestamp)
    @Query(value = "SELECT w FROM Weather w WHERE w.stationName = :city ORDER BY w.timestamp DESC")
    List<Weather> findTopByCityOrderByTimestampDesc(@Param("city") String city);
}
