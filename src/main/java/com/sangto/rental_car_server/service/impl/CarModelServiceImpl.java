package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.model.AddCarModelRequestDTO;
import com.sangto.rental_car_server.domain.entity.CarBrand;
import com.sangto.rental_car_server.domain.entity.CarModel;
import com.sangto.rental_car_server.domain.mapper.CarModelMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.CarBrandRepository;
import com.sangto.rental_car_server.repository.CarModelRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.CarModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarModelServiceImpl implements CarModelService {

    private final CarModelRepository carModelRepo;
    private final CarModelMapper carModelMapper;
    private final CarBrandRepository carBrandRepo;


    @Override
    public Response<String> addCarModel(AddCarModelRequestDTO requestDTO) {
        Optional<CarBrand> findCarBrand = carBrandRepo.findById(requestDTO.brandId());
        if (findCarBrand.isEmpty()) throw new AppException("Car brand is not found");

        Optional<CarModel> findCarModel = carModelRepo.findByName(requestDTO.modelName());
        if (findCarModel.isPresent()) throw new AppException("Car model already exists");

        CarModel newCarModel = carModelMapper.addCarModelRequestDTOtoEntity(requestDTO);
        newCarModel.setBrand(findCarBrand.get());
        carModelRepo.save(newCarModel);

        return Response.successfulResponse("Car model added successfully");
    }
}
