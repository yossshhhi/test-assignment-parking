package com.example.parking.service.impl;

import com.example.parking.entity.ParkingSession;
import com.example.parking.exception.AlreadyParkedException;
import com.example.parking.exception.NoFreeSpacesException;
import com.example.parking.mapper.ParkingSessionMapper;
import com.example.parking.repository.ParkingSessionRepository;
import com.example.parking.rest.v1.dto.request.EntryRequest;
import com.example.parking.rest.v1.dto.request.ExitRequest;
import com.example.parking.rest.v1.dto.response.EntryResponse;
import com.example.parking.rest.v1.dto.response.ExitResponse;
import com.example.parking.service.ParkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingServiceImplTest {

    @Mock
    ParkingSessionMapper mapper;
    @Mock
    ParkingSessionRepository repository;

    ParkingService service;

    @BeforeEach
    void setUp() {
        service = new ParkingServiceImpl(mapper, repository);
        ReflectionTestUtils.setField(service, "capacity", 2L);
    }

    @Test
    void registerEntry_shouldThrowAlreadyParked_whenOpenSessionExists() {
        var req = new EntryRequest();
        req.setPlateNumber(" aB1234 ");
        when(repository.findOpenByPlate("AB1234")).thenReturn(Optional.of(new ParkingSession()));

        assertThatThrownBy(() -> service.registerEntry(req))
                .isInstanceOf(AlreadyParkedException.class);

        verify(repository).findOpenByPlate("AB1234");
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void registerEntry_shouldThrowNoFreeSpaces_whenCapacityReached() {
        var req = new EntryRequest();
        req.setPlateNumber("AB1234");
        when(repository.findOpenByPlate("AB1234")).thenReturn(Optional.empty());
        when(repository.countActive()).thenReturn(2L);

        assertThatThrownBy(() -> service.registerEntry(req))
                .isInstanceOf(NoFreeSpacesException.class);

        verify(repository).countActive();
    }

    @Test
    void registerEntry_shouldSaveAndReturnResponse_whenOk() {
        var req = new EntryRequest();
        req.setPlateNumber("ab1234");

        when(repository.findOpenByPlate("AB1234")).thenReturn(Optional.empty());
        when(repository.countActive()).thenReturn(1L);

        Instant now = Instant.parse("2025-08-15T10:00:00Z");

        var entity = ParkingSession.builder()
                .id(UUID.randomUUID())
                .plateNumber("AB1234")
                .entryTime(now)
                .active(true)
                .build();

        var resp = new EntryResponse();
        resp.setEntryTime(now);

        when(mapper.toEntity(argThat(r -> "AB1234".equals(r.getPlateNumber())))).thenReturn(entity);
        when(repository.saveAndFlush(entity)).thenReturn(entity);
        when(mapper.toEntryResponse(entity)).thenReturn(resp);

        var out = service.registerEntry(req);

        assertThat(out.getEntryTime()).isEqualTo(now);
        verify(repository).findOpenByPlate("AB1234");
        verify(repository).countActive();
        verify(repository).saveAndFlush(entity);
        verify(mapper).toEntryResponse(entity);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void registerExit_shouldThrow_whenOpenSessionMissing() {
        var req = new ExitRequest();
        req.setPlateNumber("AB1234");
        when(repository.findOpenByPlate("AB1234")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.registerExit(req))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void registerExit_shouldUpdateAndReturnExitTime_whenOk() {
        var req = new ExitRequest();
        req.setPlateNumber("aB1234");
        when(repository.findOpenByPlate("AB1234")).thenReturn(Optional.of(new ParkingSession()));

        doNothing().when(repository).markExitAndGetEntry(eq("AB1234"), any(Instant.class));

        var out = service.registerExit(req);

        assertThat(out).isInstanceOf(ExitResponse.class);
        assertThat(out.getExitTime()).isNotNull();

        verify(repository).findOpenByPlate("AB1234");
        verify(repository).markExitAndGetEntry(eq("AB1234"), any(Instant.class));
    }
}