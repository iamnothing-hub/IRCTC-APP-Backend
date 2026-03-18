package com.irctc.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainDto {

    private Long trainId;

    private String trainNo;

    private String trainName;

    private Integer totalDistance;

    private StationDto sourceStation;


    private StationDto destinationStation;

    //    private String routeName;

//    private List<TrainRouteDto> trainRoutes;  //! Stoppage


//    private List<TrainSchedule>  trainSchedules;

//    @OneToOne
//    private TrainImage trainImage;


}

