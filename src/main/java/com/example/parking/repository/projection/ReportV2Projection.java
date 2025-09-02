package com.example.parking.repository.projection;

public interface ReportV2Projection {
    long getEntries();
    long getExits();
    Double getAvgStaySeconds();
}
