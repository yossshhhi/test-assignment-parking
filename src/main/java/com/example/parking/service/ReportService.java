package com.example.parking.service;

import com.example.parking.rest.v1.dto.response.ReportV1Response;
import com.example.parking.rest.v2.dto.reponse.ReportV2Response;

import java.time.Instant;

public interface ReportService {

    /**
     * Формирует отчёт версии v1.
     * Подсчитывает количество занятых и свободных мест на парковке.
     * Вычисляет среднее время пребывания автомобиля на парковке за указанный период.
     *
     * @param start начало периода
     * @param end   конец периода
     * @return объект {@link ReportV1Response} с данными отчёта
     */
    ReportV1Response reportV1(Instant start, Instant end);

    /**
     * Формирует отчёт версии v2.
     * Подсчитывает количество въездов и выездов за указанный период.
     * Вычисляет среднее время пребывания автомобиля на парковке за этот период.
     *
     * @param start начало периода (включительно)
     * @param end   конец периода (не включительно)
     * @return объект {@link ReportV2Response} с данными отчёта
     */
    ReportV2Response reportV2(Instant start, Instant end);
}
