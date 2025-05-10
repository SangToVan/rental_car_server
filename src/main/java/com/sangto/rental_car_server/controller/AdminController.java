package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import com.sangto.rental_car_server.domain.dto.brand.AddCarBrandRequestDTO;
import com.sangto.rental_car_server.domain.dto.car.AddCarRequestDTO;
import com.sangto.rental_car_server.domain.dto.car.CarDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.car.UpdCarRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.model.AddCarModelRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserResponseDTO;
import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import com.sangto.rental_car_server.responses.MetaResponse;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.*;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final CarService carService;
    private final BookingService bookingService;
    private final WalletService walletService;
    private final CarBrandService carBrandService;
    private final CarModelService carModelService;

    @GetMapping(Endpoint.V1.Admin.DETAIL_CAR)
    public ResponseEntity<Response<CarDetailResponseDTO>> getCarDetail(@PathVariable(name = "paymentId") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(carService.getCarDetail(id));
    }

    @PostMapping(Endpoint.V1.Admin.ADD_CAR)
    public ResponseEntity<Response<CarDetailResponseDTO>> addCar(
            HttpServletRequest servletRequest, @RequestBody @Valid AddCarRequestDTO requestDTO) {
        Integer idToken =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(carService.addCar(idToken, requestDTO));
    }

    @PatchMapping(Endpoint.V1.Admin.UPDATE_CAR)
    public ResponseEntity<Response<CarDetailResponseDTO>> updateCar(
            @PathVariable(name = "paymentId") Integer id, @RequestBody @Valid UpdCarRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(carService.updateCar(id, requestDTO));
    }

    @PatchMapping(Endpoint.V1.Admin.VERIFY_CAR)
    public ResponseEntity<Response<String>> verifyCar(@PathVariable(name = "paymentId", required = true) Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(carService.verifyCar(id));
    }

    @GetMapping(Endpoint.V1.Admin.USER)
    public ResponseEntity<Response<List<UserResponseDTO>>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getAllUsers());
    }

    @GetMapping(Endpoint.V1.Admin.DETAIL_USER)
    public ResponseEntity<Response<UserDetailResponseDTO>> getUserDetail(@PathVariable(name = "paymentId") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getDetailUser(id));
    }

    @GetMapping(Endpoint.V1.Admin.BOOKING)
    public ResponseEntity<MetaResponse<MetaResponseDTO, List<BookingResponseDTO>>> getAllBookings(@ParameterObject MetaRequestDTO metaRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getAllBookings(metaRequestDTO));
    }

    @GetMapping(Endpoint.V1.Admin.DETAIL_BOOKING)
    public ResponseEntity<Response<BookingDetailResponseDTO>> getBookingDetail(@PathVariable(name = "paymentId") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBookingDetail(id));
    }

    @GetMapping(Endpoint.V1.Admin.GET_WALLET)
    public ResponseEntity<Response<WalletResponseDTO>> getMyWallet(HttpServletRequest servletRequest) {
        Integer idToken =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(
                walletService.getWalletDetail(idToken)
        );
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
