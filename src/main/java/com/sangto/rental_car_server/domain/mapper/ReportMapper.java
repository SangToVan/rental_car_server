package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.report.AddReportRequestDTO;
import com.sangto.rental_car_server.domain.dto.report.ReportDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.report.UpdReportRequestDTO;
import com.sangto.rental_car_server.domain.entity.Report;

public interface ReportMapper {
    Report addReportRequestDTOtoEntity(AddReportRequestDTO requestDTO);

    ReportDetailResponseDTO toReportDetailResponseDTO(Report entity);

    Report updReportRequestDTOtoEntity(Report oldReport, UpdReportRequestDTO requestDTO);
}
