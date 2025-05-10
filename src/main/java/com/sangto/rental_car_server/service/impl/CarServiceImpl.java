package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.constant.MetaConstant;
import com.sangto.rental_car_server.domain.dto.car.*;
import com.sangto.rental_car_server.domain.dto.image.UpdImageRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.SortingDTO;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.domain.entity.Image;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import com.sangto.rental_car_server.domain.mapper.CarMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.CarRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.responses.MetaResponse;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.CarService;
import com.sangto.rental_car_server.service.CloudinaryService;
import com.sangto.rental_car_server.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Response<List<CarResponseDTO>> getListCarsForHome() {
        List<Car> list = carRepo.findTop8CarsByRating();
        List<CarResponseDTO> li = list.stream().map(carMapper::toCarResponseDTO).toList();

        return Response.successfulResponse("Get list car for home successfully", li);
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<CarResponseDTO>> getListCarsByOwnerId(MetaRequestDTO metaRequestDTO, Integer ownerId) {
        Optional<User> findUser = userRepo.findById(ownerId);
        if (findUser.isEmpty()) throw new AppException("This owner is not existed");

        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(metaRequestDTO.sortField()).ascending()
                : Sort.by(metaRequestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);
        Page<Car> page = metaRequestDTO.keyword() == null
                ? carRepo.getListCarByOwner(ownerId, pageable)
                : carRepo.getListCarByOwnerWithKeyword(ownerId, metaRequestDTO.keyword(), pageable);
        if (page.getContent().isEmpty()) throw new AppException("List car is empty");

        List<CarResponseDTO> li = page.getContent().stream()
                .map(carMapper::toCarResponseDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get list car success",
                MetaResponseDTO.builder()
                        .totalItems((int) page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .currentPage(metaRequestDTO.currentPage())
                        .pageSize(metaRequestDTO.pageSize())
                        .sorting(SortingDTO.builder()
                                .sortField(metaRequestDTO.sortField())
                                .sortDir(metaRequestDTO.sortDir())
                                .build())
                        .build(),
                li);
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<CarResponseDTO>> getAllCars(MetaRequestDTO metaRequestDTO) {
        Sort sort = metaRequestDTO.sortDir().equalsIgnoreCase(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(metaRequestDTO.sortField()).ascending()
                : Sort.by(metaRequestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);

        Page<Car> page = carRepo.findAll(pageable);

        if (page.isEmpty()) throw new AppException("No cars available");

        List<CarResponseDTO> carList = page.getContent().stream()
                .map(carMapper::toCarResponseDTO)
                .toList();

        MetaResponseDTO metaResponseDTO = MetaResponseDTO.builder()
                .totalItems((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(metaRequestDTO.currentPage())
                .pageSize(metaRequestDTO.pageSize())
                .sorting(SortingDTO.builder()
                        .sortField(metaRequestDTO.sortField())
                        .sortDir(metaRequestDTO.sortDir())
                        .build())
                .build();

        return MetaResponse.successfulResponse("Retrieved all cars successfully", metaResponseDTO, carList);
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
        Optional<Car> findCar = carRepo.findByIdWithOwner(carId);
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

        if (car.getStatus() == ECarStatus.UNVERIFIED) {
            car.setStatus(ECarStatus.ACTIVE);
        } else {
            car.setStatus(ECarStatus.UNVERIFIED);
        }
        carRepo.save(car);
        String message = car.getStatus() == ECarStatus.ACTIVE
                ? "Car with ID " + carId + " has been verified"
                : "Car with ID " + carId + " has been unverified";
        return Response.successfulResponse(message);
    }

    @Override
    public Response<String> changeCarStatus(Integer carId) {
        Optional<Car> findCar = carRepo.findById(carId);
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        Car car = findCar.get();

        if (car.getStatus() == ECarStatus.UNVERIFIED) {
            throw new AppException("This car can not change status");
        } else if (car.getStatus() == ECarStatus.SUSPENDED) {
            car.setStatus(ECarStatus.ACTIVE);
        } else {
            car.setStatus(ECarStatus.SUSPENDED);
        }
        carRepo.save(car);
        String message = car.getStatus() == ECarStatus.ACTIVE
                ? "Car with ID " + carId + " has been activated"
                : "Car with ID " + carId + " has been suspended";
        return Response.successfulResponse(message);
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<CarResponseDTO>> searchCarV2(String address, String startTime, String endTime, MetaRequestDTO metaRequestDTO) {
        String field = metaRequestDTO.sortField();
        if (field.compareTo(MetaConstant.Sorting.DEFAULT_FIELD) == 0) field = "car_id";
        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(field).ascending()
                : Sort.by(field).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);
        Page<Car> page = carRepo.searchCarV2(address, startTime, endTime, pageable);

        if (page.getContent().isEmpty()) throw new AppException("List car is empty");
        List<CarResponseDTO> li = page.getContent().stream()
                .map(carMapper::toCarResponseDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Search car success",
                MetaResponseDTO.builder()
                        .totalItems((int) page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .currentPage(metaRequestDTO.currentPage())
                        .pageSize(metaRequestDTO.pageSize())
                        .sorting(SortingDTO.builder()
                                .sortField(metaRequestDTO.sortField())
                                .sortDir(metaRequestDTO.sortDir())
                                .build())
                        .build(),
                li);
    }
}
