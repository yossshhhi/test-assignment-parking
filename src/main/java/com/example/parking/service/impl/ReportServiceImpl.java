package com.example.parking.service.impl;

import com.example.parking.repository.ParkingSessionRepository;
import com.example.parking.rest.v1.dto.response.ReportV1Response;
import com.example.parking.rest.v2.dto.reponse.ReportV2Response;
import com.example.parking.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ParkingSessionRepository repository;

    @Value("${parking.capacity}")
    private long capacity;

    @Override
    @Transactional(readOnly = true)
    public ReportV1Response reportV1(Instant start, Instant end) {
        var projection = repository.aggregatedReportV1(start, end);
        long occupied = projection.getOccupied();
        long free = Math.max(0, capacity - occupied);

        return ReportV1Response.builder()
                .occupied(occupied)
                .free(free)
                .avgStaySeconds(Math.round(projection.getAvgStaySeconds()))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ReportV2Response reportV2(Instant start, Instant end) {
        var projection = repository.aggregatedReportV2(start, end);
        return ReportV2Response.builder()
                .entries(projection.getEntries())
                .exits(projection.getExits())
                .avgStaySeconds(Math.round(projection.getAvgStaySeconds()))
                .build();
    }
}
