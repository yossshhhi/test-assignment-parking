package com.example.parking.repository;

import com.example.parking.entity.ParkingSession;
import com.example.parking.repository.projection.ReportV1Projection;
import com.example.parking.repository.projection.ReportV2Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface ParkingSessionRepository extends JpaRepository<ParkingSession, UUID> {

    @Query("select ps from ParkingSession ps where ps.plateNumber = :plate and ps.active = true")
    Optional<ParkingSession> findOpenByPlate(String plate);

    @Modifying
    @Query(value = """
        update parking_session
           set exit_time = :now, active = false
         where plate_number = :plate
           and active = true
    """, nativeQuery = true)
    void markExitAndGetEntry(String plate, Instant now);

    @Query("select count(ps) from ParkingSession ps where ps.active = true")
    long countActive();

    @Query(value = """
        select
            coalesce(sum(case when active = true then 1 else 0 end), 0) as occupied,
            coalesce(avg(extract(epoch from (exit_time - entry_time)))
              filter (where exit_time >= :start and exit_time < :end), 0) as avgStaySeconds
        from parking_session
    """, nativeQuery = true)
    ReportV1Projection aggregatedReportV1(Instant start, Instant end);

    @Query(value = """
      select
        coalesce(sum(case when entry_time >= :start and entry_time < :end then 1 else 0 end), 0) as entries,
        coalesce(sum(case when exit_time  >= :start and exit_time  < :end then 1 else 0 end), 0) as exits,
        coalesce(avg(extract(epoch from (exit_time - entry_time)))
          filter (where exit_time >= :start and exit_time < :end), 0) as avgStaySeconds
      from parking_session
    """, nativeQuery = true)
    ReportV2Projection aggregatedReportV2(Instant start, Instant end);
}
