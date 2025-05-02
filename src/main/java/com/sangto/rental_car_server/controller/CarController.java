package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseForOwnerDTO;
import com.sangto.rental_car_server.domain.dto.car.*;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.BookingService;
import com.sangto.rental_car_server.service.CarService;
import com.sangto.rental_car_server.utility.AuthUtil;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cars")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CarController {

    private final JwtTokenUtil jwtTokenUtil;
    private final CarService carService;
    private final BookingService bookingService;

    @GetMapping(Endpoint.V1.Car.BASE)
    public ResponseEntity<Response<List<CarResponseDTO>>> getAllCars() {
        System.out.println("getAllCars");
        return ResponseEntity.status(HttpStatus.OK).body(carService.getAllCars());
    }

    @PostMapping(Endpoint.V1.Car.BASE)
    public ResponseEntity<Response<CarDetailResponseDTO>> addCar(
            HttpServletRequest servletRequest, @RequestBody @Valid AddCarRequestDTO requestDTO) {
        Integer idToken =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(carService.addCar(idToken, requestDTO));
    }

    @GetMapping(Endpoint.V1.Car.GET_LIST_FOR_OWNER)
    public ResponseEntity<Response<List<CarResponseDTO>>> getListCarForOwner(
            HttpServletRequest servletRequest) {
        Integer ownerId =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(carService.getListCarsByOwnerId(ownerId));
    }

    @GetMapping(Endpoint.V1.Car.DETAILS)
    public ResponseEntity<Response<CarDetailResponseDTO>> getCarDetail(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(carService.getCarDetail(id));
    }

    @GetMapping(Endpoint.V1.Car.DETAILS_FOR_OWNER)
    public ResponseEntity<Response<CarDetailResponseForOwnerDTO>> getCarDetailForOwner(
            @PathVariable(name = "id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(carService.getCarDetailForOwner(id));
    }

    @PatchMapping(Endpoint.V1.Car.DETAILS_FOR_OWNER)
    public ResponseEntity<Response<CarDetailResponseDTO>> updateCar(
            @PathVariable(name = "id") Integer id, @RequestBody @Valid UpdCarRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(carService.updateCar(id, requestDTO));
    }

    @PatchMapping(Endpoint.V1.Car.STATUS)
    public ResponseEntity<Response<String>> changeStatus(@PathVariable(name = "id") Integer carId) {
        User user =
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(carService.changeCarStatus(carId));
    }

    @GetMapping(Endpoint.V1.Car.LIST_CAR_BOOKINGS)
    public ResponseEntity<Response<List<BookingResponseForOwnerDTO>>> getListBookingByCarId(
            @PathVariable(name = "id") Integer carId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.getAllBookingForCar(carId, AuthUtil.getRequestedUser().getId()));
    }
}
