package com.irctc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "trains")
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trainNo;

    private String trainName;

    private Integer totalDistance;

    @ManyToOne          //? Many Trains can have same source station.
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @ManyToOne          //? Many Trains can have same destination station.
    @JoinColumn(name = "destination_station_id")
    private Station destinationStation;

//    private String routeName;
    @OneToMany(mappedBy = "train")
    private List<TrainRoute> trainRoutes;  //! Stoppage

    @OneToMany(mappedBy = "train")
    private List<TrainSchedule>  trainSchedules;

//    @OneToOne
//    private TrainImage trainImage;




































}
