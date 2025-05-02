package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import com.sangto.rental_car_server.domain.dto.car.AddCarRequestDTO;
import com.sangto.rental_car_server.domain.dto.car.CarDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.car.UpdCarRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserResponseDTO;
import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.BookingService;
import com.sangto.rental_car_server.service.CarService;
import com.sangto.rental_car_server.service.UserService;
import com.sangto.rental_car_server.service.WalletService;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping(Endpoint.V1.Admin.DETAIL_CAR)
    public ResponseEntity<Response<CarDetailResponseDTO>> getCarDetail(@PathVariable(name = "id") Integer id) {
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
            @PathVariable(name = "id") Integer id, @RequestBody @Valid UpdCarRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(carService.updateCar(id, requestDTO));
    }

    @PatchMapping(Endpoint.V1.Admin.VERIFY_CAR)
    public ResponseEntity<Response<String>> verifyCar(@PathVariable(name = "id", required = true) Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(carService.verifyCar(id));
    }

    @GetMapping(Endpoint.V1.Admin.USER)
    public ResponseEntity<Response<List<UserResponseDTO>>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getAllUsers());
    }

    @GetMapping(Endpoint.V1.Admin.DETAIL_USER)
    public ResponseEntity<Response<UserDetailResponseDTO>> getUserDetail(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getDetailUser(id));
    }

    @GetMapping(Endpoint.V1.Admin.BOOKING)
    public ResponseEntity<Response<List<BookingResponseDTO>>> getAllBookings() {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getAllBookings());
    }

    @GetMapping(Endpoint.V1.Admin.DETAIL_BOOKING)
    public ResponseEntity<Response<BookingDetailResponseDTO>> getBookingDetail(@PathVariable(name = "id") Integer id) {
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
}
