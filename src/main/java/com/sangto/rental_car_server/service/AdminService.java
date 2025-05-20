package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.admin.*;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.car.CarDetailResponseDTO;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.responses.Response;

import java.util.List;

public interface AdminService {
    Response<DashboardResponseDTO> getDashboard();

    Response<CarResponseForAdminDTO> getListCar(String status);

    Response<CarDetailResponseDTO> getCarDetail(Integer carId);

    Response<BookingResponseForAdminDTO> getListBooking(String status);

    Response<BookingDetailResponseDTO> getBookingDetail(Integer bookingId);

    Response<EscrowResponseForAdminDTO> getListEscrow();

    Response<UserResponseForAdminDTO> getListUser(EUserRole userRole);

    Response<UserDetailResponseForAdminDTO> getUserDetail(Integer userId);

    Response<String> verifyLicense(Integer userId);

    Response<String> unverifyLicense(Integer userId);
}
