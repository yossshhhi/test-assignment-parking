package com.example.parking.service.impl;

import com.example.parking.repository.ParkingSessionRepository;
import com.example.parking.repository.projection.ReportV1Projection;
import com.example.parking.repository.projection.ReportV2Projection;
import com.example.parking.rest.v1.dto.response.ReportV1Response;
import com.example.parking.rest.v2.dto.reponse.ReportV2Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

class ReportServiceImplTest {

    @Mock
    ParkingSessionRepository repository;

    ReportServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ReportServiceImpl(repository);
        ReflectionTestUtils.setField(service, "capacity", 100L);
    }

    @Test
    void reportV1_ok() {
        var start = Instant.parse("2025-08-15T00:00:00Z");
        var end = Instant.parse("2025-08-16T00:00:00Z");

        var proj = new ReportV1Projection() {
            @Override public long getOccupied() { return 30L; }
            @Override public Double getAvgStaySeconds() { return 120.6; }
        };

        when(repository.aggregatedReportV1(start, end)).thenReturn(proj);

        ReportV1Response r = service.reportV1(start, end);

        assertThat(r.getOccupied()).isEqualTo(30L);
        assertThat(r.getFree()).isEqualTo(70L);
        assertThat(r.getAvgStaySeconds()).isEqualTo(121L);
    }

    @Test
    void reportV2_ok() {
        var start = Instant.parse("2025-08-15T00:00:00Z");
        var end = Instant.parse("2025-08-16T00:00:00Z");

        var proj = new ReportV2Projection() {
            @Override public long getEntries() { return 10L; }
            @Override public long getExits() { return 8L; }
            @Override public Double getAvgStaySeconds() { return 89.4; }
        };

        when(repository.aggregatedReportV2(start, end)).thenReturn(proj);

        ReportV2Response r = service.reportV2(start, end);

        assertThat(r.getEntries()).isEqualTo(10L);
        assertThat(r.getExits()).isEqualTo(8L);
        assertThat(r.getAvgStaySeconds()).isEqualTo(89L);
    }
}