package com.example.parking.service.impl;

import com.example.parking.exception.AlreadyParkedException;
import com.example.parking.exception.NoFreeSpacesException;
import com.example.parking.mapper.ParkingSessionMapper;
import com.example.parking.repository.ParkingSessionRepository;
import com.example.parking.rest.v1.dto.request.EntryRequest;
import com.example.parking.rest.v1.dto.request.ExitRequest;
import com.example.parking.rest.v1.dto.response.EntryResponse;
import com.example.parking.rest.v1.dto.response.ExitResponse;
import com.example.parking.service.ParkingService;
import com.example.parking.util.PlateNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private final ParkingSessionMapper mapper;
    private final ParkingSessionRepository repository;

    @Value("${parking.capacity}")
    private long capacity;

    @Override
    @Transactional
    public EntryResponse registerEntry(EntryRequest req) {
        req.setPlateNumber(PlateNormalizer.normalize(req.getPlateNumber()));
        repository.findOpenByPlate(req.getPlateNumber())
                .ifPresent(ps -> {
                    throw new AlreadyParkedException(req.getPlateNumber());
                });

        checkFreeSpaces();

        var session = repository.saveAndFlush(mapper.toEntity(req));
        return mapper.toEntryResponse(session);
    }

    @Override
    @Transactional
    public ExitResponse registerExit(ExitRequest req) {
        req.setPlateNumber(PlateNormalizer.normalize(req.getPlateNumber()));
        repository.findOpenByPlate(req.getPlateNumber())
                .orElseThrow(() -> new NoSuchElementException("Open session not found for plate: " + req.getPlateNumber()));

        var exitTime = Instant.now();
        repository.markExitAndGetEntry(req.getPlateNumber(), exitTime);

        return ExitResponse.builder()
                .exitTime(exitTime)
                .build();
    }

    private void checkFreeSpaces() {
        long active = repository.countActive();
        long free = Math.max(0, capacity - active);
        if (free <= 0) {
            throw new NoFreeSpacesException();
        }
    }
}
