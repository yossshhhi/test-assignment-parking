package com.example.parking.rest.v1.dto.request;

import com.example.parking.model.VehicleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EntryRequest {

    @NotBlank
    @Pattern(regexp = "[A-Z0-9-]{3,16}")
    @Schema(description = "Номер автомобиля.")
    private String plateNumber;

    @NotNull
    @Schema(description = "Тип автомобиля.")
    private VehicleType vehicleType;
}
