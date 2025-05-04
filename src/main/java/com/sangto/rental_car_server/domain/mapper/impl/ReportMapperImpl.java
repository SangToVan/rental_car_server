package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.report.AddReportRequestDTO;
import com.sangto.rental_car_server.domain.dto.report.ReportResponseDTO;
import com.sangto.rental_car_server.domain.dto.report.UpdReportRequestDTO;
import com.sangto.rental_car_server.domain.entity.Report;
import com.sangto.rental_car_server.domain.mapper.ReportMapper;
import com.sangto.rental_car_server.repository.BookingRepository;
import com.sangto.rental_car_server.repository.ReportRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportMapperImpl implements ReportMapper {

    private final ReportRepository reportRepo;
    private final UserRepository userRepo;
    private final BookingRepository bookingRepo;

    @Override
    public Report addReportRequestDTOtoEntity(AddReportRequestDTO requestDTO) {
        return Report.builder()
                .reporter(userRepo.findById(requestDTO.reporterId()).orElse(null))
                .booking(bookingRepo.findById(requestDTO.bookingId()).orElse(null))
                .targetType(requestDTO.targetType())
                .targetId(requestDTO.targetId())
                .content(requestDTO.content())
                .build();
    }

    @Override
    public ReportResponseDTO toReportResponseDTO(Report entity) {
        return ReportResponseDTO.builder()
                .reportId(entity.getId())
                .reporterId(entity.getReporter().getId())
                .bookingId(entity.getBooking().getId())
                .targetType(entity.getTargetType())
                .targetId(entity.getTargetId())
                .content(entity.getContent())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .resolvedAt(entity.getResolvedAt())
                .adminNote(entity.getAdminNote())
                .build();
    }

    @Override
    public Report updReportRequestDTOtoEntity(Report oldReport, UpdReportRequestDTO requestDTO) {

        oldReport.setStatus(requestDTO.status());
        oldReport.setAdminNote(requestDTO.adminNote());

        return oldReport;
    }
}
