package org.example.delivery.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * This class represents the Weather entity in the database
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    /**
     * The name of the weather station
     */
    private String stationName;

    /**
     * The wmo code of the station
     */
    private String wmo;

    /**
     * The air temperature
     */
    private float temperature;

    /**
     * The wind speed
     */
    private float windSpeed;

    /**
     * The weather phenomenon
     */
    private String phenomenon;

    /**
     * The timestamp of the observation
     */
    private String timestamp;
}
