package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseForOwnerDTO;
import com.sangto.rental_car_server.domain.dto.car.*;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.responses.MetaResponse;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public ResponseEntity<MetaResponse<MetaResponseDTO, List<CarResponseDTO>>> searchCars(
            @ParameterObject @Valid SearchCarRequestDTO requestDTO,
            @ParameterObject MetaRequestDTO metaRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(carService.searchCarV2(
                        requestDTO.address(),
                        requestDTO.startTime(),
                        requestDTO.endTime(),
                        requestDTO.brand(),
                        requestDTO.numberOfSeats(),
                        requestDTO.transmission(),
                        requestDTO.fuelType(),
                        requestDTO.minPrice(),
                        requestDTO.maxPrice(),
                        metaRequestDTO
                ));
    }

    @PostMapping(Endpoint.V1.Car.BASE)
    public ResponseEntity<Response<CarDetailResponseDTO>> addCar(
            HttpServletRequest servletRequest, @RequestBody @Valid AddCarRequestDTO requestDTO) {
        Integer idToken =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(carService.addCar(idToken, requestDTO));
    }

    @GetMapping(Endpoint.V1.Car.GET_LIST_FOR_OWNER)
    public ResponseEntity<MetaResponse<MetaResponseDTO, List<CarResponseForOwnerDTO>>> getListCarForOwner(
            HttpServletRequest servletRequest, @ParameterObject MetaRequestDTO requestDTO) {
        Integer ownerId =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(carService.getListCarsByOwnerId(requestDTO, ownerId));
    }

    @GetMapping(Endpoint.V1.Car.DETAILS)
    public ResponseEntity<Response<CarDetailResponseForBookingDTO>> getCarDetail(
            @PathVariable(name = "carId") Integer id,
            @RequestParam(name = "startDateTime", required = false) String startDateTimeStr,
            @RequestParam(name = "endDateTime", required = false) String endDateTimeStr) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = null;

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            User user = (User) auth.getPrincipal();
            userId = user.getId();
        }

        return ResponseEntity.status(HttpStatus.OK).body(carService.getCarDetailForBooking(id, userId, startDateTimeStr, endDateTimeStr));
    }


    @GetMapping(Endpoint.V1.Car.DETAILS_FOR_OWNER)
    public ResponseEntity<Response<CarDetailResponseForOwnerDTO>> getCarDetailForOwner(
            @PathVariable(name = "carId") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(carService.getCarDetailForOwner(id));
    }

    @GetMapping(Endpoint.V1.Car.REGISTER)
    public ResponseEntity<Response<CarRegisterDetailResponseDTO>> getRegisterCar(@PathVariable(name = "carId") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(carService.getCarRegisterDetail(id));
    }

    @PatchMapping(Endpoint.V1.Car.REGISTER)
    public ResponseEntity<Response<CarDetailResponseDTO>> registerCar(@PathVariable(name = "carId") Integer id, @RequestBody @Valid AddCarRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(carService.registerCar(id, requestDTO));
    }

    @PatchMapping(Endpoint.V1.Car.DETAILS_FOR_OWNER)
    public ResponseEntity<Response<CarDetailResponseDTO>> updateCar(
            @PathVariable(name = "carId") Integer id, @RequestBody @Valid UpdCarRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(carService.updateCar(id, requestDTO));
    }

    @PatchMapping(Endpoint.V1.Car.UPD_INFO)
    public ResponseEntity<Response<CarDetailResponseForOwnerDTO>> updateCarInfo(
            @PathVariable(name = "carId") Integer id, @RequestBody @Valid UpdCarInfoRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(carService.updateCarInfo(id, requestDTO));
    }

    @PatchMapping(Endpoint.V1.Car.UPD_PRICING)
    public ResponseEntity<Response<CarDetailResponseForOwnerDTO>> updateCarPricing(
            @PathVariable(name = "carId") Integer id, @RequestBody @Valid UpdCarPricingRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(carService.updateCarPricing(id, requestDTO));
    }

    @PatchMapping(Endpoint.V1.Car.STATUS)
    public ResponseEntity<Response<String>> changeStatus(@PathVariable(name = "carId") Integer carId) {
        User user =
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(carService.changeCarStatus(carId));
    }

    @GetMapping(Endpoint.V1.Car.LIST_CAR_BOOKINGS)
    public ResponseEntity<MetaResponse<MetaResponseDTO, List<BookingResponseForOwnerDTO>>> getListBookingByCarId(
            @PathVariable(name = "carId") Integer carId, @ParameterObject MetaRequestDTO metaRequestDTO, @RequestParam(value = "status", required = false) EBookingStatus status) {
        if(status == null) {
            return ResponseEntity.status(HttpStatus.OK).body(bookingService.getAllBookingForCar(metaRequestDTO, carId, AuthUtil.getRequestedUser().getId()));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.getAllBookingForCarByStatus(metaRequestDTO, carId, AuthUtil.getRequestedUser().getId(), status));
    }
}
