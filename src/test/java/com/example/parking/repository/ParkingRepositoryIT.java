package com.example.parking.repository;

import com.example.parking.ParkingApplication;
import com.example.parking.PostgreSQLContainerInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ParkingApplication.class)
@ContextConfiguration(initializers = {PostgreSQLContainerInitializer.class})
@Testcontainers(disabledWithoutDocker = true)
class ParkingRepositoryIT {

    @Autowired
    ParkingSessionRepository repository;
    @Autowired
    JdbcTemplate jdbc;

    @Test
    @Transactional
    void shouldCountActiveAndAggregateReports() {
        var now = Instant.parse("2025-08-15T10:00:00Z");

        jdbc.update("""
            insert into parking_session(id, plate_number, vehicle_type, entry_time, active)
            values (?, ?, ?, ?, true)
        """, UUID.randomUUID(), "AAA111", "CAR", Timestamp.from(now.minusSeconds(600)));

        jdbc.update("""
            insert into parking_session(id, plate_number, vehicle_type, entry_time, active)
            values (?, ?, ?, ?, true)
        """, UUID.randomUUID(), "BBB222", "CAR", Timestamp.from(now.minusSeconds(300)));

        jdbc.update("""
            insert into parking_session(id, plate_number, vehicle_type, entry_time, exit_time, active)
            values (?, ?, ?, ?, ?, false)
        """, UUID.randomUUID(), "CCC333", "CAR",
                Timestamp.from(now.minusSeconds(3600)),
                Timestamp.from(now.minusSeconds(1200)));

        Long active = repository.countActive();
        assertThat(active).isEqualTo(2L);

        var start = now.minusSeconds(7200);
        var end = now.plusSeconds(1);

        var v1 = repository.aggregatedReportV1(start, end);
        assertThat(v1.getOccupied()).isEqualTo(2L);
        assertThat(Math.round(v1.getAvgStaySeconds())).isEqualTo(2400L);

        var v2 = repository.aggregatedReportV2(start, end);
        assertThat(v2.getEntries()).isEqualTo(3L);
        assertThat(v2.getExits()).isEqualTo(1L);
        assertThat(Math.round(v2.getAvgStaySeconds())).isEqualTo(2400L);
    }

    @Test
    @Transactional
    void shouldMarkExitByPlate() {
        var now = Instant.parse("2025-08-15T12:00:00Z");

        jdbc.update("""
            insert into parking_session(id, plate_number, vehicle_type, entry_time, active)
            values (?, ?, ?, ?, true)
        """, UUID.randomUUID(), "DDD444", "CAR", Timestamp.from(now.minusSeconds(900)));

        repository.markExitAndGetEntry("DDD444", now);

        assertThat(repository.findOpenByPlate("DDD444")).isEmpty();

        Long exits = jdbc.queryForObject("""
            select count(*) from parking_session
             where plate_number='DDD444' and active=false and exit_time is not null
        """, Long.class);
        assertThat(exits).isEqualTo(1L);
    }
}