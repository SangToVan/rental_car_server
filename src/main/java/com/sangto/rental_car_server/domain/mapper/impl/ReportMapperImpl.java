package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.dto.report.AddReportRequestDTO;
import com.sangto.rental_car_server.domain.dto.report.ReportDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.report.UpdReportRequestDTO;
import com.sangto.rental_car_server.domain.entity.Report;
import com.sangto.rental_car_server.domain.enums.EReporter;
import com.sangto.rental_car_server.domain.mapper.ImageMapper;
import com.sangto.rental_car_server.domain.mapper.ReportMapper;
import com.sangto.rental_car_server.repository.BookingRepository;
import com.sangto.rental_car_server.repository.ReportRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ReportMapperImpl implements ReportMapper {

    private final ReportRepository reportRepo;
    private final UserRepository userRepo;
    private final BookingRepository bookingRepo;
    private final ImageMapper imageMapper;

    @Override
    public Report addReportRequestDTOtoEntity(AddReportRequestDTO requestDTO) {
        return Report.builder()
                .content(requestDTO.content())
                .images(new ArrayList<>())
                .build();
    }

    @Override
    public ReportDetailResponseDTO toReportDetailResponseDTO(Report entity) {

        List<ImageResponseDTO> imageList = entity.getImages().stream()
                .map(imageMapper::toImageResponseDTO)
                .toList();

        EReporter reporter = Objects.equals(entity.getUser().getId(), entity.getBooking().getUser().getId()) ? EReporter.CUSTOMER : EReporter.OWNER;

        return ReportDetailResponseDTO.builder()
                .reportId(entity.getId())
                .reporter(reporter)
                .content(entity.getContent())
                .images(imageList)
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
        oldReport.setResolvedAt(LocalDateTime.now());

        return oldReport;
    }
}
