package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.brand.AddCarBrandRequestDTO;
import com.sangto.rental_car_server.domain.entity.CarBrand;
import com.sangto.rental_car_server.domain.mapper.CarBrandMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.CarBrandRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.CarBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarBrandServiceImpl implements CarBrandService {

    private final CarBrandRepository carBrandRepo;
    private final CarBrandMapper carBrandMapper;

    @Override
    public Response<String> addCarBrand(AddCarBrandRequestDTO requestDTO) {
        Optional<CarBrand> findCarBrand = carBrandRepo.findByName(requestDTO.brandName());
        if (findCarBrand.isPresent()) throw new AppException("Car brand already exists");

        CarBrand newCarBrand = carBrandMapper.addCarBrandRequestDTOtoEntity(requestDTO);
        carBrandRepo.save(newCarBrand);

        return Response.successfulResponse("Car brand added successfully");
    }
}
