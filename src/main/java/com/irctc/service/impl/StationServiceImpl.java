package com.irctc.service.impl;

import com.irctc.dto.StationDto;
import com.irctc.entity.Station;
import com.irctc.exception.DuplicateFieldException;
import com.irctc.exception.ResourceNotFoundException;
import com.irctc.repository.StationRepository;
import com.irctc.service.StationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "station_service")
@Slf4j
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
        log.info("Station is created successfully with name: {}", station.getStationName());
        return modelMapper.map(station, StationDto.class);
    }

    /**
     * Retrieves a station by its unique database ID.
     *
     * @param id The primary key ID of the station to find.
     * @return The found station converted to a StationDto.
     * @throws ResourceNotFoundException If no station exists with the provided ID.
     */
    @Override
    public StationDto findStation(Long id) {
        Station station = stationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Station is not found."));
        return modelMapper.map(station, StationDto.class);
    }

    /**
     * Searches for stations based on a provided keyword matching station names or codes.
     *
     * @param keyword The search term used to filter stations.
     * @return A list of StationDto objects matching the search criteria; returns an empty list if no matches found.
     */
    @Override
    public List<StationDto> searchStation(String keyword) {
        List<Station> stationList = stationRepository.searchStations(keyword);
        List<StationDto> stationDtoList = stationList.stream().map(station -> modelMapper.map(station, StationDto.class)).toList();
        return stationDtoList;
    }

    /**
     * Retrieves a paginated and sorted list of all stations.
     *
     * @param pageNumber The zero-based page index to retrieve.
     * @param pageSize   The number of records per page.
     * @param sortBy     The field name by which the results should be sorted.
     * @param sortDir    The direction of sorting, either "ASC" (ascending) or "DESC" (descending).
     * @return A Page object containing StationDto elements and pagination metadata.
     */
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

    /**
     * Updates an existing station's details based on the provided ID and DTO data.
     *
     * @param stationDto The DTO containing the updated information (Name, Code, City, State).
     * @param id         The unique identifier of the station to be updated.
     * @return A success message if updated, or a failure message if the save operation fails.
     * @throws ResourceNotFoundException If no station exists with the given ID.
     */
    @Override
    public String updateStation(StationDto stationDto, Long id) {
        Station station = stationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Station is not found for this Id: " + id));
        station.setStationName(stationDto.getStationName());
        station.setStationCode(stationDto.getStationCode());
        station.setCity(stationDto.getCity());
        station.setState(stationDto.getState());

        Station station1 = stationRepository.save(station);
        if(station1 == null){
            log.error("Station updation is failed");
            return "Station is not updated.";
        }
        log.info("Station is updated successfully: {}", station1.getStationName());
        return "Station has been updated.";
    }
}
