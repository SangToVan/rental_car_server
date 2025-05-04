package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.report.AddReportRequestDTO;
import com.sangto.rental_car_server.domain.dto.report.UpdReportRequestDTO;
import com.sangto.rental_car_server.responses.Response;

public interface ReportService {
    Response<String> addReport(AddReportRequestDTO requestDTO);

    Response<String> updateReport(UpdReportRequestDTO requestDTO);

}
