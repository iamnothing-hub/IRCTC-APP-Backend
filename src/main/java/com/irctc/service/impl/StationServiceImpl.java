package com.irctc.service.impl;

import com.irctc.dto.StationDto;
import com.irctc.entity.Station;
import com.irctc.exception.DuplicateFieldException;
import com.irctc.exception.ResourceNotFoundException;
import com.irctc.repository.StationRepository;
import com.irctc.service.StationService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "station_service")
public class StationServiceImpl implements StationService {

    private StationRepository stationRepository;
    private ModelMapper modelMapper;

    public StationServiceImpl(StationRepository stationRepository, ModelMapper modelMapper) {
        this.stationRepository = stationRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a new station record after validating for duplicates.
     *
     * @param stationDto The station data transfer object containing details to be saved.
     * @return The persisted station converted back to a DTO.
     * @throws DuplicateFieldException If a station with the same station code already exists in the database.
     */
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

    @Override
    public List<StationDto> searchStation(String keyword) {
        List<Station> stationList = stationRepository.searchStations(keyword);
        List<StationDto> stationDtoList = stationList.stream().map(station -> modelMapper.map(station, StationDto.class)).toList();
        return stationDtoList;
    }

    @Override
    public Page<StationDto> getAllStations(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<Station> stations = stationRepository.findAll(pageRequest);

        Page<StationDto> stationDtos = stations.map(station ->
            modelMapper.map(station, StationDto.class)
        );
        return stationDtos;
    }

    @Override
    public String updateStation(StationDto stationDto, Long id) {
        Station station = stationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Station is not found for this Id: " + id));
        station.setStationName(stationDto.getStationName());
        station.setStationCode(stationDto.getStationCode());
        station.setCity(stationDto.getCity());
        station.setState(stationDto.getState());

        Station station1 = stationRepository.save(station);
        if(station1.equals(null)){
            return "Station is not updated.";
        }
        return "Station has been updated.";
    }
}
