package com.irctc.dto;

import com.irctc.entity.Station;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainWithStationsProjection {
    private Long trainId;

    private String trainNo;

    private String trainName;

    private Integer totalDistance;

    private StationDto sourceStation;

    private StationDto destinationStation;
}
