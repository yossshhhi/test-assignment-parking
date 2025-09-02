package com.example.parking.rest.v1;

import com.example.parking.rest.v1.dto.response.ReportV1Response;
import com.example.parking.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Tag(name = "Отчетность по парковке", description = "Интерфейс для получения отчетов по парковке.")
@RestController
@RequestMapping("/api/v1/parking")
@RequiredArgsConstructor
public class ReportControllerV1 {

    private final ReportService reportService;

    @GetMapping("/report")
    @Operation(operationId = "report", summary = "Получение отчета со статистикой по посещению парковки за определенный период времени.")
    public ReportV1Response report(
            @RequestParam("start_date") Instant start,
            @RequestParam("end_date") Instant end) {
        return reportService.reportV1(start, end);
    }
}
