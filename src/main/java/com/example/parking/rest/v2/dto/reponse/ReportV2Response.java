package com.example.parking.rest.v2.dto.reponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportV2Response {

    @Schema(description = "Количество въехавших на парковку автомобилей.")
    private long entries;

    @Schema(description = "Количество выехавших с парковки автомобилей.")
    private long exits;

    @Schema(description = "Среднее время пребывания автомобилей на парковке.")
    private long avgStaySeconds;

}
