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
        Train alreadyExistTrain = trainRepository.findByTrainNo(trainDto.getTrainNo());
        if(alreadyExistTrain !=null) throw new DuplicateFieldException("Train is already present with " + trainDto.getTrainNo());
        Train train = trainRepository.save(mapper.map(trainDto, Train.class));
        log.info("Train is created successfully with trainNo is: ", train.getTrainNo());
//        if(train.equals(null)) throw new DuplicateFieldException("Train is already present");
        return "Train added successfully" ;
    }

    @Override
    public List<TrainDto> getTrainByNameOrNumber(String keyword) {
        List<Train> trains = trainRepository.findByTrainNoOrTrainName(keyword);
        trains = trains.stream().filter(train -> train.getActive() == true).toList();
        if(trains.size()==0) throw new ResourceNotFoundException("Train not found with given keyword: "+ keyword);

        List<TrainDto> trainDtos = trains.stream().map(train -> mapper.map(train, TrainDto.class)).toList();
        return trainDtos;
    }

    @Override
    public String removeTrain(Long id) {
        Train train = trainRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Train not found with given ID: " + id));
        train.setActive(false);
        Train deletedTrain = trainRepository.save(train);
        log.info("Train is deleted successfully, Train Number is: ", train.getTrainNo());
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
