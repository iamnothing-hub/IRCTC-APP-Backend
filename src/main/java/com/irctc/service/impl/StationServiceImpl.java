package com.irctc.service.impl;

import com.irctc.dto.StationDto;
import com.irctc.entity.Station;
import com.irctc.exception.DuplicateFieldException;
import com.irctc.exception.ResourceNotFoundException;
import com.irctc.repository.StationRepository;
import com.irctc.service.StationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service(value = "station_service")
public class StationServiceImpl implements StationService {

    private StationRepository stationRepository;
    private ModelMapper modelMapper;

    public StationServiceImpl(StationRepository stationRepository, ModelMapper modelMapper) {
        this.stationRepository = stationRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public StationDto createStation(StationDto stationDto) {
//        System.out.println(stationDto.toString());
        Station stationByStationCode = stationRepository.findByStationCode(stationDto.getStationCode());
        if(stationByStationCode!=null) throw  new DuplicateFieldException("This station is already exist for " + stationDto.getStationCode() + " code");
        Station station = stationRepository.save(modelMapper.map(stationDto, Station.class));
        return modelMapper.map(station, StationDto.class);
    }

    @Override
    public StationDto findStation(Long id) {
        Station station = stationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Station is not found."));
        return modelMapper.map(station, StationDto.class);
    }
}
