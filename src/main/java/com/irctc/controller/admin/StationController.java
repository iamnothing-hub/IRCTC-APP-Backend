package com.irctc.controller.admin;

import com.irctc.dto.StationDto;
import com.irctc.response.GenericResponse;
import com.irctc.service.StationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    // Get all stations and use pagination and search criteria


    // Update Station

    // Delete Station
}
