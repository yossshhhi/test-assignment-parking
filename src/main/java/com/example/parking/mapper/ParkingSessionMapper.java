package com.example.parking.mapper;

import com.example.parking.entity.ParkingSession;
import com.example.parking.rest.v1.dto.request.EntryRequest;
import com.example.parking.rest.v1.dto.response.EntryResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface ParkingSessionMapper {

    /**
     * Преобразовывает {@link EntryRequest} в объект {@link ParkingSession}.
     *
     * @param request Данные о новом автомобиле, который заезжает на парковку.
     * @return объект {@link ParkingSession}
     */
    ParkingSession toEntity(EntryRequest request);

    /**
     * Преобразовывает {@link ParkingSession} в объект {@link EntryResponse}.
     *
     * @param parkingSession Данные о парковке автомобиля.
     * @return объект {@link EntryResponse}
     */
    EntryResponse toEntryResponse(ParkingSession parkingSession);

    @AfterMapping
    default void afterMapping(@MappingTarget ParkingSession parkingSession) {
        parkingSession.setActive(true);
    }
}
