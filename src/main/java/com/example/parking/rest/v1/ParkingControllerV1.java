package com.example.parking.rest.v1;

import com.example.parking.rest.v1.dto.request.EntryRequest;
import com.example.parking.rest.v1.dto.request.ExitRequest;
import com.example.parking.rest.v1.dto.response.EntryResponse;
import com.example.parking.rest.v1.dto.response.ExitResponse;
import com.example.parking.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Управление парковкой", description = "Интерфейс для регистрации и выезда автомобилей на парковке.")
@RestController
@RequestMapping("/api/v1/parking")
@RequiredArgsConstructor
public class ParkingControllerV1 {

    private final ParkingService parkingService;

    @PostMapping("/entry")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "entry", summary = "Регистрация автомобиля на парковке.")
    public EntryResponse entry(@Valid @RequestBody EntryRequest request) {
        return parkingService.registerEntry(request);
    }

    @PostMapping("/exit")
    @Operation(operationId = "exit", summary = "Выезд автомобиля с парковки.")
    public ExitResponse exit(@Valid @RequestBody ExitRequest request) {
        return parkingService.registerExit(request);
    }
}
