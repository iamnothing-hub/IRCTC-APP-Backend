package com.irctc.service;

import com.irctc.dto.StationDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StationService {

    StationDto createStation(StationDto stationDto);
    StationDto findStation(Long id);
    List<StationDto> searchStation(String keyword);
    Page<StationDto> getAllStations(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    String updateStation(StationDto stationDto, Long id);

}
