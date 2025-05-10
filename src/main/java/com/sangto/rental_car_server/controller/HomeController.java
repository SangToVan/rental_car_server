package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.car.CarResponseDTO;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.CarService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Home")
@RestController
@RequiredArgsConstructor
public class HomeController {
    private final CarService carService;

    @GetMapping(Endpoint.V1.Home.CAR)
    public ResponseEntity<Response<List<CarResponseDTO>>> getListCar() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(carService.getListCarsForHome());
    }
}
