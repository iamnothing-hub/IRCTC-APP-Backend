package com.irctc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "train_routes")
public class TrainRoute {    //! Stoppage

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne                  //? One station can have many Train Routes
    @JoinColumn(name = "station_id")
    private Station station;

    private Integer stationOrder;

    private LocalTime arrivalTime;

    private LocalTime departureTime;

    private Integer haltMinutes;

    private Integer distanceFromSource;

    @ManyToOne                  //? Many Routes can be a single Train
    @JoinColumn(name = "train_id")
    private Train train;






}
