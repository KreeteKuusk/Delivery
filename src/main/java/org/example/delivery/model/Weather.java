package org.example.delivery.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Weather")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String stationName;
    private String vmo;
    private float temperature;
    private float windSpeed;
    private String phenomenon;
    private String timestamp;


/*
• Name of the station
• WMO code of the station
• Air temperature
• Wind speed
• Weather phenomenon
• Timestamp of the observations
*/
}
