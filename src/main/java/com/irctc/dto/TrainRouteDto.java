package com.irctc.dto;

import com.irctc.entity.Station;
import com.irctc.entity.Train;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainRouteDto {    //! Stoppage

    private Long id;

    private StationDto station;

    private Integer stationOrder;

    private LocalTime arrivalTime;

    private LocalTime departureTime;

    private Integer haltMinutes;

    private Integer distanceFromSource;

    private TrainDto train;






}
