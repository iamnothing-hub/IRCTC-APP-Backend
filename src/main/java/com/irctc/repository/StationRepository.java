package com.irctc.repository;

import com.irctc.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {

    Station findByStationCode(String code);

    @Query("""
        SELECT s FROM Station s
        WHERE LOWER(s.stationName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(s.stationCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
    List<Station> searchStations(@Param("keyword") String keyword);
}
