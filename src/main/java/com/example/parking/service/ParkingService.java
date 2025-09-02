package com.example.parking.service;

import com.example.parking.rest.v1.dto.request.EntryRequest;
import com.example.parking.rest.v1.dto.request.ExitRequest;
import com.example.parking.rest.v1.dto.response.EntryResponse;
import com.example.parking.rest.v1.dto.response.ExitResponse;

public interface ParkingService {

    /**
     * Регистрирует въезд автомобиля на парковку.
     *
     * @param req запрос с данными автомобиля ({@link EntryRequest})
     * @return ответ с временем въезда ({@link EntryResponse})
     */
    EntryResponse registerEntry(EntryRequest req);

    /**
     * Регистрирует выезд автомобиля с парковки.
     *
     * @param req запрос с данными автомобиля ({@link ExitRequest})
     * @return ответ с временем выезда ({@link ExitResponse})
     */
    ExitResponse registerExit(ExitRequest req);
}
