package com.irctc.service;

import com.irctc.dto.StationDto;

public interface StationService {

    StationDto createStation(StationDto stationDto);
    StationDto findStation(Long id);

}
