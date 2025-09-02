package com.example.parking.rest.v1.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Data
public class EntryResponse {

    @Schema(description = "Время заезда.")
    private Instant entryTime;
}