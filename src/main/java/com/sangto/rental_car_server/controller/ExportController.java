package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.service.ExportService;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Tag(name = "Export")
@RestController
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping(Endpoint.V1.User.EXPORT_EXCEL)
    public void exportRevenue(
            HttpServletRequest servletRequest,
            @RequestParam(value = "startDate", required = false) String startDateStr,
            @RequestParam(value = "endDate", required = false) String endDateStr,
            HttpServletResponse response
    ) throws IOException {
        Integer userId = Integer.valueOf(
                jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION))
        );

        // Parse string → LocalDateTime, cho phép null
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        try {
            if (startDateStr != null) {
                startDate = LocalDateTime.parse(startDateStr, formatter);
            }
            if (endDateStr != null) {
                endDate = LocalDateTime.parse(endDateStr, formatter);
            }
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Ngày không đúng định dạng yyyy-MM-dd'T'HH:mm:ss");
        }

        response.setContentType("application/vnd.ms-excel");
        String headerValue = "attachment; filename=Thongkedoanhthu_" + userId + ".xlsx";
        response.setHeader("Content-Disposition", headerValue);

        exportService.exportRevenue(userId, startDate, endDate, response.getOutputStream());
    }
}
