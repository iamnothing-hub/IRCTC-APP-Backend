package com.irctc.service;

import com.irctc.dto.TrainDto;
import com.irctc.response.GenericResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TrainService {

    String createTrain(TrainDto trainDto);

    List<TrainDto> getTrainByNameOrNumber(String keyword);

    String removeTrain(Long id);

    String updateTrain(TrainDto trainDto, Long id);

    List<TrainDto> findTrainBetweenTwoStation(Long sourceStationId, Long destinationStationId);



}
