package com.irctc.service.impl;

import com.irctc.dto.TrainDto;
//import com.irctc.service.AdminTrainService;
import com.irctc.entity.Station;
import com.irctc.entity.Train;
import com.irctc.exception.DuplicateFieldException;
import com.irctc.exception.ResourceNotFoundException;
import com.irctc.repository.TrainRepository;
import com.irctc.response.GenericResponse;
import com.irctc.service.TrainService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "train_service")
//@AllArgsConstructor
//@NoArgsConstructor
@Slf4j
public class TrainServiceImpl implements TrainService {

    private TrainRepository trainRepository;

    private ModelMapper mapper;

    public TrainServiceImpl(TrainRepository trainRepository, ModelMapper mapper) {
        this.trainRepository = trainRepository;
        this.mapper = mapper;
    }

    @Override
    public String createTrain(TrainDto trainDto) {
        log.info("Attempting to create a new train with Train No: {}", trainDto.getTrainNo());

        Train alreadyExistTrain = trainRepository.findByTrainNo(trainDto.getTrainNo());
        if (alreadyExistTrain != null) {
            log.warn("Create Train failed: Train No {} already exists in the database", trainDto.getTrainNo());
            throw new DuplicateFieldException("Train is already present with " + trainDto.getTrainNo());
        }
        Train savedTrain = trainRepository.save(mapper.map(trainDto, Train.class));
        log.info("Train created successfully: [Train No: {}, Name: {}]", savedTrain.getTrainNo(), savedTrain.getTrainName());
        return "Train added successfully";
    }

    @Override
    public List<TrainDto> getTrainByNameOrNumber(String keyword) {
        log.info("Searching for trains with keyword: '{}'", keyword);
        List<Train> trains = trainRepository.findByTrainNoOrTrainName(keyword);
        // Filter active trains
        List<Train> activeTrains = trains.stream()
                .filter(train -> Boolean.TRUE.equals(train.getActive()))
                .toList();
        if (activeTrains.isEmpty()) {
            // Negative scenario: Either no match at all, or matching trains are inactive
            log.warn("Search failed: No active trains found for keyword '{}' (Total matches: {})",
                    keyword, trains.size());
            throw new ResourceNotFoundException("Train not found with given keyword: " + keyword);
        }
        // Positive scenario
        log.info("Successfully found {} active train(s) for keyword: '{}'", activeTrains.size(), keyword);
        return activeTrains.stream()
                .map(train -> mapper.map(train, TrainDto.class))
                .toList();
    }

    @Override
    public String removeTrain(Long id) {
        Train train = trainRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Train not found with given ID: " + id));
        train.setActive(false);
        Train deletedTrain = trainRepository.save(train);
        log.info("Train is deleted successfully, Train Number is: {}", train.getTrainNo());
        return "Train has been deleted successfully.";
    }

    @Override
    public String updateTrain(TrainDto trainDto, Long id) {
        Train train = trainRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Train not found with given ID: " + id));
        train.setTrainName(trainDto.getTrainName());
        train.setSourceStation(mapper.map(trainDto.getSourceStation(), Station.class));
        train.setDestinationStation(mapper.map(trainDto.getDestinationStation(), Station.class));
        train.setTotalDistance(trainDto.getTotalDistance());
        Train updatedTrain = trainRepository.save(train);
        return "Train has been updated successfully.";
    }

    @Override
    public List<TrainDto> findTrainBetweenTwoStation(Long sourceStationId, Long destinationStationId) {
        List<Train> trains = trainRepository.findBySourceStationIdAndDestinationStationId(sourceStationId, destinationStationId);
        if(trains.size()==0) throw new ResourceNotFoundException("Train not found.");

        List<TrainDto> trainDtos = trains.stream().map(train -> mapper.map(train, TrainDto.class)).toList();
        return trainDtos;
    }
}
