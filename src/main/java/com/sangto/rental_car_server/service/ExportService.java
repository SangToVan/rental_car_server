package com.sangto.rental_car_server.service;

import jakarta.servlet.ServletOutputStream;

import java.io.IOException;
import java.time.LocalDateTime;

public interface ExportService {
    public void exportRevenue(Integer ownerId, LocalDateTime startDate, LocalDateTime endDate, ServletOutputStream out) throws IOException;
}
