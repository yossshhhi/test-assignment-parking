package com.example.parking.entity;

import com.example.parking.model.VehicleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

/**
 * Парковка.
 *
 * @author Ussenova Arailym.
 */
@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "parking_session")
public class ParkingSession {

    /**
     * Идентификатор.
     */
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    /**
     * Номер автомобиля.
     */
    @Column(name = "plate_number", nullable = false, updatable=false, length = 16)
    private String plateNumber;

    /**
     * Тип автомобиля.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, updatable=false, length = 16)
    private VehicleType vehicleType;

    /**
     * Время заезда.
     */
    @CreationTimestamp
    @Column(name = "entry_time", nullable = false, updatable = false)
    private Instant entryTime;

    /**
     * Время выезда.
     */
    @Column(name = "exit_time")
    private Instant exitTime;

    /**
     * Флаг активности парковки.
     */
    @Column(name = "active", nullable = false)
    private Boolean active;
}
