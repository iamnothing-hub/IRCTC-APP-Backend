package com.irctc.controller.admin;


import com.irctc.dto.TrainDto;
import com.irctc.response.GenericResponse;
import com.irctc.service.TrainService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@AllArgsConstructor
//@NoArgsConstructor
@Slf4j
@RestController
@RequestMapping("/admin/train")
public class AdminTrainController {

    private TrainService trainService;

    private Logger logger;

    public AdminTrainController(TrainService trainService) {
        this.trainService = trainService;

    }

    @PostMapping
    public ResponseEntity<GenericResponse<String>> createTrain(@RequestBody TrainDto trainDto){
        String message = trainService.createTrain(trainDto);
//        logger.info(message);
        return ResponseEntity.ok().body(
                GenericResponse.success(message)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<GenericResponse<Object>> searchTrain(@RequestParam String keyword){
        List<TrainDto> train = trainService.getTrainByNameOrNumber(keyword);
        return ResponseEntity.ok().body(
                GenericResponse.builder()
                        .success(true)
                        .data(train)
                        .message("Train searched successfully.")
                        .build()
        );
    }

    @DeleteMapping("/delete/{trainId}")
    public ResponseEntity<GenericResponse<String>> removeTrain(@PathVariable Long trainId){
        String message = trainService.removeTrain(trainId);
        return ResponseEntity.ok().body(
                GenericResponse.success(message)
        );
    }

    @PutMapping("/update/{trainId}")
    public ResponseEntity<GenericResponse<String>> updateTrain(@RequestBody TrainDto trainDto, @PathVariable Long trainId){
        String message = trainService.updateTrain(trainDto,trainId);
        return ResponseEntity.ok().body(
                GenericResponse.success(message)
        );
    }

    @GetMapping("/search/{sourceId}/{destId}")
    public ResponseEntity<GenericResponse<Object>> findTrainBetweenTwoStations(@PathVariable Long sourceId, @PathVariable Long destId ){
        List<TrainDto> trains = trainService.findTrainBetweenTwoStation(sourceId, destId);
        return ResponseEntity.ok().body(
                GenericResponse.builder()
                        .success(true)
                        .data(trains)
                        .message("Train searched successfully.")
                        .build()
        );
    }
}
