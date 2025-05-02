package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.car.*;
import com.sangto.rental_car_server.domain.dto.image.UpdImageRequestDTO;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.domain.entity.Image;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import com.sangto.rental_car_server.domain.mapper.CarMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.CarRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.CarService;
import com.sangto.rental_car_server.service.CloudinaryService;
import com.sangto.rental_car_server.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final UserRepository userRepo;
    private final CarRepository carRepo;
    private final CarMapper carMapper;
    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;

    @Value("${cloudinary.folder.car}")
    private String carFolder;

    @Override
    public Car verifyCarOwner(Integer ownerId, Integer carId) {
        Optional<Car> findCar = carRepo.findById(carId);
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        Car car = findCar.get();
        if (!Objects.equals(car.getCarOwner().getId(), ownerId)) throw new AppException("Unauthorized");
        return car;
    }

    @Override
    public Response<List<CarResponseDTO>> getListCarsByOwnerId(Integer ownerId) {
        Optional<User> findUser = userRepo.findById(ownerId);
        if (findUser.isEmpty()) throw new AppException("This owner is not existed");
        List<Car> cars = carRepo.getListCarByOwner(ownerId);
        List<CarResponseDTO> li = cars.stream().map(carMapper::toCarResponseDTO).toList();

        return Response.successfulResponse(
                "Get list car successfully",
                li
        );
    }

    @Override
    public Response<List<CarResponseDTO>> getAllCars() {
        List<Car> cars = carRepo.getAllCars();
        List<CarResponseDTO> li = cars.stream().map(carMapper::toCarResponseDTO).toList();

        return Response.successfulResponse(
                "Get list car successfully",
                li
        );
    }

    @Override
    public Response<CarDetailResponseDTO> getCarDetail(Integer carId) {
        Optional<Car> findCar = carRepo.findById(carId);
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        return Response.successfulResponse(
                "Get car detail successfully", carMapper.toCarDetailResponseDTO(findCar.get())
        );
    }

    @Override
    public Response<CarDetailResponseForOwnerDTO> getCarDetailForOwner(Integer carId) {
        Optional<Car> findCar = carRepo.findById(carId);
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        return Response.successfulResponse(
                "Get car detail for owner successfully", carMapper.toCarDetailResponseForOwnerDTO(findCar.get())
        );
    }

    @Override
    public Response<CarDetailResponseDTO> addCar(Integer ownerId, AddCarRequestDTO requestDTO) {
        Optional<User> findUser = userRepo.findById(ownerId);
        if (findUser.isEmpty()) throw new AppException("This owner is not existed");

        Car newCar = carMapper.addCarRequestDTOtoEntity(requestDTO);
        newCar.setCarOwner(findUser.get());

        // Set Image for Car
        try {
            for (String item : requestDTO.images()) {
                Map resultUpload = cloudinaryService.uploadFileBase64(item, carFolder);
                Image imageUpload = Image.builder()
                        .name((String) resultUpload.get("original_filename"))
                        .imageUrl((String) resultUpload.get("url"))
                        .imagePublicId((String) resultUpload.get("public_id"))
                        .createdAt(LocalDate.now())
                        .build();
                newCar.addImage(imageUpload);
            }
            Car saveCar = carRepo.save(newCar);
            return Response.successfulResponse(
                    "Add car successfully", carMapper.toCarDetailResponseDTO(saveCar)
            );
        } catch (IOException e) {
            throw new AppException("Add new car unsuccessfully");
        }
    }

    @Override
    public Response<CarDetailResponseDTO> updateCar(Integer carId, UpdCarRequestDTO requestDTO) {
        Optional<Car> oldCar = carRepo.findById(carId);
        if (oldCar.isEmpty()) throw new AppException("This car is not existed");
        Car newCar = carMapper.updCarRequestDTOtoEntity(oldCar.get(), requestDTO);

        // Update Image For Car
        if (requestDTO.images().length > 0) {
            for (UpdImageRequestDTO item : requestDTO.images()) {
                imageService.updImage(item, carFolder);
            }
        }
        List<Image> newImage = carRepo.findById(carId).get().getImages();
        newCar.setImages(newImage);
        Car savedCar = carRepo.save(newCar);
        return Response.successfulResponse(
                "Update car successfully", carMapper.toCarDetailResponseDTO(savedCar)
        );
    }

    @Override
    public Response<String> verifyCar(Integer carId) {
        Optional<Car> findCar = carRepo.findById(carId);
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        Car car = findCar.get();

        if (car.getCarStatus() == ECarStatus.UNVERIFIED) {
            car.setCarStatus(ECarStatus.ACTIVE);
        } else {
            car.setCarStatus(ECarStatus.UNVERIFIED);
        }
        carRepo.save(car);
        String message = car.getCarStatus() == ECarStatus.ACTIVE
                ? "Car with ID " + carId + " has been verified"
                : "Car with ID " + carId + " has been unverified";
        return Response.successfulResponse(message);
    }

    @Override
    public Response<String> changeCarStatus(Integer carId) {
        Optional<Car> findCar = carRepo.findById(carId);
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        Car car = findCar.get();

        if (car.getCarStatus() == ECarStatus.UNVERIFIED) {
            throw new AppException("This car can not change status");
        } else if (car.getCarStatus() == ECarStatus.SUSPENDED) {
            car.setCarStatus(ECarStatus.ACTIVE);
        } else {
            car.setCarStatus(ECarStatus.SUSPENDED);
        }
        carRepo.save(car);
        String message = car.getCarStatus() == ECarStatus.ACTIVE
                ? "Car with ID " + carId + " has been activated"
                : "Car with ID " + carId + " has been suspended";
        return Response.successfulResponse(message);
    }
}
