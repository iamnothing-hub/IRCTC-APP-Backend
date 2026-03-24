package com.irctc.repository;

import com.irctc.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainRepository extends JpaRepository<Train, Long> {

    Train findByTrainNo(String trainNo);

    @Query("""
        SELECT t FROM Train t
        WHERE LOWER(t.trainName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(t.trainNo) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
    List<Train> findByTrainNoOrTrainName(@Param("keyword") String keyword);

    @Query("""
                SELECT t FROM Train t
                WHERE t.sourceStation.id = :sourceStationId\s
                AND t.destinationStation.id = :destinationStationId\s
                AND t.active = true
            """)
    List<Train> findBySourceStationIdAndDestinationStationId( @Param("sourceStationId") Long sourceStationId, @Param("destinationStationId") Long destinationStationId);

}
