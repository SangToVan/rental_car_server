package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.admin.*;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.brand.AddCarBrandRequestDTO;
import com.sangto.rental_car_server.domain.dto.car.CarDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.model.AddCarModelRequestDTO;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final CarService carService;
    private final CarBrandService carBrandService;
    private final CarModelService carModelService;

    @GetMapping(Endpoint.V1.Admin.DASHBOARD)
    public ResponseEntity<Response<DashboardResponseDTO>> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboard());
    }

    @GetMapping(Endpoint.V1.Admin.CAR)
    public ResponseEntity<Response<CarResponseForAdminDTO>> getCar(@RequestParam("status") String status) {
        return ResponseEntity.ok(adminService.getListCar(status));
    }

    @GetMapping(Endpoint.V1.Admin.DETAIL_CAR)
    public ResponseEntity<Response<CarDetailResponseDTO>> getDetailCar(@PathVariable(name = "carId") Integer carId) {
        return ResponseEntity.ok(adminService.getCarDetail(carId));
    }

    @PatchMapping(Endpoint.V1.Admin.VERIFY_CAR)
    public ResponseEntity<Response<String>> verifyCar(@PathVariable(name = "carId") Integer carId) {
        return ResponseEntity.ok(carService.verifyCar(carId));
    }

    @GetMapping(Endpoint.V1.Admin.BOOKING)
    public ResponseEntity<Response<BookingResponseForAdminDTO>> getBooking(@RequestParam("status") String status) {
        return ResponseEntity.ok(adminService.getListBooking(status));
    }

    @GetMapping(Endpoint.V1.Admin.DETAIL_BOOKING)
    public ResponseEntity<Response<BookingDetailResponseDTO>> getBookingDetail(@PathVariable(name = "bookingId") Integer bookingId) {
        return ResponseEntity.ok(adminService.getBookingDetail(bookingId));
    }

    @GetMapping(Endpoint.V1.Admin.USER)
    public ResponseEntity<Response<UserResponseForAdminDTO>> getUser(@RequestParam("role") EUserRole role) {
        return ResponseEntity.ok(adminService.getListUser(role));
    }

    @GetMapping(Endpoint.V1.Admin.DETAIL_USER)
    public ResponseEntity<Response<UserDetailResponseForAdminDTO>> getDetailUser(@PathVariable(name = "userId") Integer userId) {
        return ResponseEntity.ok(adminService.getUserDetail(userId));
    }

    @PatchMapping(Endpoint.V1.Admin.VERIFY_USER)
    public ResponseEntity<Response<String>> verifyLicense(@PathVariable(name = "userId") Integer userId) {
        return ResponseEntity.ok(adminService.verifyLicense(userId));
    }

    @PatchMapping(Endpoint.V1.Admin.UNVERIFY_USER)
    public ResponseEntity<Response<String>> unverifyLicense(@PathVariable(name = "userId") Integer userId) {
        return ResponseEntity.ok(adminService.unverifyLicense(userId));
    }

    @GetMapping(Endpoint.V1.Admin.ESCROW)
    public ResponseEntity<Response<EscrowResponseForAdminDTO>> getEscrow(){
        return ResponseEntity.ok(adminService.getListEscrow());
    }

    @PostMapping(Endpoint.V1.Admin.BRAND)
    public ResponseEntity<Response<String>> addBrand(AddCarBrandRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(carBrandService.addCarBrand(requestDTO));
    }

    @PostMapping(Endpoint.V1.Admin.MODEL)
    public ResponseEntity<Response<String>> addModel(AddCarModelRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(carModelService.addCarModel(requestDTO));
    }
}
