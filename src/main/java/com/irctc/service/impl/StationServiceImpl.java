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
        if(stationByStationCode!=null) {
            log.error("Failed to create station: Station code {} already exists", stationDto.getStationCode());
            throw new DuplicateFieldException("This station is already exist for " + stationDto.getStationCode() + " code");
        }
        Station station = stationRepository.save(modelMapper.map(stationDto, Station.class));
        log.info("Station created successfully: [Name: {}, Code: {}]", station.getStationName(), station.getStationCode());
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
        Station station = stationRepository.findById(id).orElseThrow(() -> {
            log.warn("Station lookup failed: ID {} not found", id);
            return new ResourceNotFoundException("Station is not found.");
        });

        log.info("Station retrieved successfully for ID: {}", id);
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
        log.info("Searching for stations with keyword: '{}'", keyword);

        List<Station> stationList = stationRepository.searchStations(keyword);

        if (stationList.isEmpty()) {
            log.warn("No stations found matching keyword: '{}'", keyword);
        } else {
            log.info("Found {} stations for keyword: '{}'", stationList.size(), keyword);
        }

        return stationList.stream()
                .map(station -> modelMapper.map(station, StationDto.class))
                .toList();
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
        log.info("Fetching stations - Page: {}, Size: {}, SortBy: {}, Direction: {}", pageNumber, pageSize, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<Station> stations = stationRepository.findAll(pageRequest);

        if (stations.isEmpty()) {
            log.warn("No stations found for the requested page: {}", pageNumber);
        } else {
            log.info("Successfully retrieved {} stations (Total Elements: {}, Total Pages: {})",
                    stations.getNumberOfElements(), stations.getTotalElements(), stations.getTotalPages());
        }
        return stations.map(station -> modelMapper.map(station, StationDto.class));
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
        log.info("Attempting to update station with ID: {}", id);

        // Negative scenario: Station not found
        Station station = stationRepository.findById(id).orElseThrow(() -> {
            log.error("Update failed: Station with ID {} not found", id);
            return new ResourceNotFoundException("Station is not found for this Id: " + id);
        });

        station.setStationName(stationDto.getStationName());
        station.setStationCode(stationDto.getStationCode());
        station.setCity(stationDto.getCity());
        station.setState(stationDto.getState());

        try {
            Station updatedStation = stationRepository.save(station);
            // Positive scenario
            log.info("Station updated successfully: [ID: {}, Name: {}]", id, updatedStation.getStationName());
            return "Station has been updated.";
        } catch (Exception e) {
            // Negative scenario: Database/Persistence failure
            log.error("Update failed for Station ID {}: {}", id, e.getMessage());
            return "Station is not updated.";
        }
    }
}
