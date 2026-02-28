package com.irctc.controller.admin;

import com.irctc.dto.StationDto;
import com.irctc.response.GenericResponse;
import com.irctc.service.StationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/station")
public class StationController {

    private StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse<StationDto>> createStation( @Valid @RequestBody StationDto stationDto){

        StationDto station = stationService.createStation(stationDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GenericResponse.success("Station created successfully.", station));
    }

    // Find Station by Id
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<Object>> getStation(@PathVariable Long id){
        StationDto station = stationService.findStation(id);
        return ResponseEntity.ok().body(GenericResponse.builder()
                .success(true)
                .message("Train is fetched successfully")
                .data(station)
                .build()
        );
    }

    // Find By Station Name and Code by Search
    @GetMapping("/search")
    public ResponseEntity<GenericResponse<Object>> searchStation(@RequestParam String keyword){
        List<StationDto> stationDtos = stationService.searchStation(keyword);
        return ResponseEntity.ok().body(
                GenericResponse.builder()
                        .success(true)
                        .message("Train fetched successfully.")
                        .data(stationDtos)
                        .build()
        );
    }


    // Get all stations and use pagination and search criteria
    @GetMapping
    public ResponseEntity<GenericResponse<Object>> getAllStation(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "stationName") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ){
        Page<StationDto> stationDtos = stationService.getAllStations(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok().body(
                GenericResponse.builder()
                        .success(true)
                        .message("All Trains fetched successfully.")
                        .data(stationDtos)
                        .build()
        );
    }

    // Update Station

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse<Object>> updateStation(@RequestBody StationDto stationDto, @PathVariable Long id){
        String s = stationService.updateStation(stationDto, id);
        return ResponseEntity.ok().body(
                GenericResponse.builder()
                        .success(true)
                        .message(s)
                        .data(null)
                        .build()
        );
    }


    // Delete Station
}
