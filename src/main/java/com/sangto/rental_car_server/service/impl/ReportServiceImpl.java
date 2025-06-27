package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.report.AddReportRequestDTO;
import com.sangto.rental_car_server.domain.dto.report.UpdReportRequestDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.entity.Image;
import com.sangto.rental_car_server.domain.entity.Report;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.mapper.ReportMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.BookingRepository;
import com.sangto.rental_car_server.repository.ReportRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.CloudinaryService;
import com.sangto.rental_car_server.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepo;
    private final UserRepository userRepo;
    private final BookingRepository bookingRepo;
    private final ReportMapper reportMapper;
    private final CloudinaryService cloudinaryService;

    @Value("${cloudinary.folder.car}")
    private String carFolder;

    @Override
    public Response<String> addReport(Integer userId, Integer bookingId, AddReportRequestDTO requestDTO) {

        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("Reporter not found");

        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("Booking not found");

        Report newReport = reportMapper.addReportRequestDTOtoEntity(requestDTO);
        newReport.setUser(findUser.get());
        newReport.setBooking(findBooking.get());

        try {
            for (String item : requestDTO.images()) {
                Map resultUpload = cloudinaryService.uploadFileBase64(item, carFolder);
                Image imageUpload = Image.builder()
                        .name((String) resultUpload.get("original_filename"))
                        .imageUrl((String) resultUpload.get("url"))
                        .imagePublicId((String) resultUpload.get("public_id"))
                        .createdAt(LocalDate.now())
                        .build();
                newReport.addImage(imageUpload);
            }
            reportRepo.save(newReport);
            return Response.successfulResponse("Report added successfully");
        } catch (IOException e) {
            throw new AppException( "Can not add report "+ e.getMessage());
        }
    }

    @Override
    public Response<String> updateReport(UpdReportRequestDTO requestDTO) {

        Optional<Report> findReport = reportRepo.findById(requestDTO.reportId());
        if (findReport.isEmpty()) throw new AppException("Report not found");

        Report newReport = reportMapper.updReportRequestDTOtoEntity(findReport.get(), requestDTO);
        reportRepo.save(newReport);

        return Response.successfulResponse("Report updated successfully");
    }
}
