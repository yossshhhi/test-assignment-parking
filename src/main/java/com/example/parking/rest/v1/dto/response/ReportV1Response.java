package com.example.parking.rest.v1.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportV1Response {

    @Schema(description = "Количество занятых мест.")
    private long occupied;

    @Schema(description = "Количество свободных мест.")
    private long free;

    @Schema(description = "Среднее время пребывания автомобилей на парковке.")
    private long avgStaySeconds;
}

