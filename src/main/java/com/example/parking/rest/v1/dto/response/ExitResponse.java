package com.example.parking.rest.v1.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ExitResponse {

    @Schema(description = "Время выезда.")
    private Instant exitTime;
}
