package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.car.*;
import com.sangto.rental_car_server.domain.dto.feedback.FeedbackResponseDTO;
import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import com.sangto.rental_car_server.domain.mapper.CarMapper;
import com.sangto.rental_car_server.domain.mapper.FeedbackMapper;
import com.sangto.rental_car_server.domain.mapper.ImageMapper;
import com.sangto.rental_car_server.domain.mapper.UserMapper;
import com.sangto.rental_car_server.repository.BookingRepository;
import com.sangto.rental_car_server.repository.CarRepository;
import com.sangto.rental_car_server.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CarMapperImpl implements CarMapper {

    private final UserMapper userMapper;
    private final ImageMapper imageMapper;
    private final FeedbackMapper feedbackMapper;
    //private final CarModelRepository carModelRepo;
    private final BookingRepository bookingRepo;
    private final FeedbackRepository feedbackRepo;
    private final CarRepository carRepo;

    @Override
    public CarResponseDTO toCarResponseDTO(Car entity) {
        List<ImageResponseDTO> imageList = entity.getImages().stream()
                .map(imageMapper::toImageResponseDTO)
                .toList();
        return CarResponseDTO.builder()
                .carId(entity.getId())
                .name(entity.getName())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .transmission(entity.getTransmission())
                .numberOfSeats(entity.getNumberOfSeats())
                .fuelType(entity.getFuelType())
                .address(entity.getAddress())
                .basePrice(entity.getBasePrice().toBigInteger())
                .rating(feedbackRepo.getRatingByCarId(entity.getId()))
                .bookingCount(bookingRepo.countCompletedBookingsByCarId(entity.getId()))
                .images(imageList)
                .build();
    }

    @Override
    public CarResponseForOwnerDTO toCarResponseForOwnerDTO(Car entity) {

        List<ImageResponseDTO> imageList = entity.getImages().stream()
                .map(imageMapper::toImageResponseDTO)
                .toList();

        return CarResponseForOwnerDTO.builder()
                .carId(entity.getId())
                .status(entity.getStatus())
                .name(entity.getName())
                .address(entity.getAddress())
                .rating(feedbackRepo.getRatingByCarId(entity.getId()))
                .completeBookingCount(bookingRepo.countCompletedBookingsByCarId(entity.getId()))
                .inProgressBookingCount(bookingRepo.countInProgressBookingsByCarId(entity.getId()))
                .pendingBookingCount(bookingRepo.countPendingBookingsByCarId(entity.getId()))
                .basePrice(entity.getBasePrice().toBigInteger())
                .images(imageList)
                .build();
    }

    @Override
    public CarDetailResponseDTO toCarDetailResponseDTO(Car entity, Integer userId) {
        List<ImageResponseDTO> imageList = entity.getImages().stream()
                .map(imageMapper::toImageResponseDTO)
                .toList();

        List<FeedbackResponseDTO> feedbackList = feedbackRepo.getListByCarId(entity.getId()).stream()
                .map(feedbackMapper::toFeedbackResponseDTO).toList();

        return CarDetailResponseDTO.builder()
                .carId(entity.getId())
                .name(entity.getName())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .color(entity.getColor())
                .numberOfSeats(entity.getNumberOfSeats())
                .productionYear(entity.getProductionYear())
                .transmission(entity.getTransmission())
                .fuelType(entity.getFuelType())
                .mileage(entity.getMileage())
                .fuelConsumption(entity.getFuelConsumption())
                .address(entity.getAddress())
                .description(entity.getDescription())
                .additionalFunctions(entity.getAdditionalFunctions())
                .termOfUse(entity.getTermsOfUse())
                .createdAt(entity.getCreatedAt().toString())
                .updatedAt(entity.getUpdatedAt().toString())
                .basePrice(entity.getBasePrice().toBigInteger())
                .maxDeliveryDistance(entity.getMaxDeliveryDistance())
                .deliveryFee(entity.getDeliveryFee())
                .freeDeliveryDistance(entity.getFreeDeliveryDistance())
                .kmPerDay(entity.getKmPerDay())
                .kmOverDayFee(entity.getKmOverDayFee())
                .status(entity.getStatus())
                .rating(feedbackRepo.getRatingByCarId(entity.getId()))
                .bookingCount(bookingRepo.countCompletedBookingsByCarId(entity.getId()))
                .carOwner(userMapper.toUserDetailResponseDTO(entity.getCarOwner()))
                .feedbacks(feedbackList)
                .images(imageList)
                .isOwner(Objects.equals(entity.getCarOwner().getId(), userId))
                .build();
    }

    @Override
    public CarDetailResponseForBookingDTO toCarDetailResponseForBookingDTO(Car entity, Integer userId, String startDateTime, String endDateTime) {
        List<ImageResponseDTO> imageList = entity.getImages().stream()
                .map(imageMapper::toImageResponseDTO)
                .toList();

        List<FeedbackResponseDTO> feedbackList = feedbackRepo.getListByCarId(entity.getId()).stream()
                .map(feedbackMapper::toFeedbackResponseDTO).toList();

        Optional<Car> checkSchedule = carRepo.checkScheduleCar(entity.getId(), startDateTime, endDateTime);

        return CarDetailResponseForBookingDTO.builder()
                .carId(entity.getId())
                .name(entity.getName())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .color(entity.getColor())
                .numberOfSeats(entity.getNumberOfSeats())
                .productionYear(entity.getProductionYear())
                .transmission(entity.getTransmission())
                .fuelType(entity.getFuelType())
                .mileage(entity.getMileage())
                .fuelConsumption(entity.getFuelConsumption())
                .address(entity.getAddress())
                .description(entity.getDescription())
                .additionalFunctions(entity.getAdditionalFunctions())
                .termOfUse(entity.getTermsOfUse())
                .createdAt(entity.getCreatedAt().toString())
                .updatedAt(entity.getUpdatedAt().toString())
                .basePrice(entity.getBasePrice().toBigInteger())
                .maxDeliveryDistance(entity.getMaxDeliveryDistance())
                .deliveryFee(entity.getDeliveryFee())
                .freeDeliveryDistance(entity.getFreeDeliveryDistance())
                .kmPerDay(entity.getKmPerDay())
                .kmOverDayFee(entity.getKmOverDayFee())
                .status(entity.getStatus())
                .rating(feedbackRepo.getRatingByCarId(entity.getId()))
                .bookingCount(bookingRepo.countCompletedBookingsByCarId(entity.getId()))
                .carOwner(userMapper.toUserDetailResponseDTO(entity.getCarOwner()))
                .feedbacks(feedbackList)
                .images(imageList)
                .isOwner(Objects.equals(entity.getCarOwner().getId(), userId))
                .canBooking(checkSchedule.isPresent())
                .build();
    }

    @Override
    public CarRegisterDetailResponseDTO toCarRegisterDetailResponseDTO(Car entity) {
        List<ImageResponseDTO> imageList = entity.getImages().stream()
                .map(imageMapper::toImageResponseDTO)
                .toList();

        return CarRegisterDetailResponseDTO.builder()
                .name(entity.getName())
                .licensePlate(entity.getLicensePlate())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .color(entity.getColor())
                .numberOfSeats(entity.getNumberOfSeats())
                .productionYear(entity.getProductionYear())
                .transmission(entity.getTransmission())
                .fuelType(entity.getFuelType())
                .mileage(entity.getMileage())
                .fuelConsumption(entity.getFuelConsumption())
                .address(entity.getAddress())
                .description(entity.getDescription())
                .additionalFunctions(entity.getAdditionalFunctions())
                .termOfUse(entity.getTermsOfUse())
                .basePrice(entity.getBasePrice().toBigInteger())
                .quickRent(entity.getQuickRent())
                .maxDeliveryDistance(entity.getMaxDeliveryDistance())
                .deliveryFee(entity.getDeliveryFee())
                .freeDeliveryDistance(entity.getFreeDeliveryDistance())
                .kmPerDay(entity.getKmPerDay())
                .kmOverDayFee(entity.getKmOverDayFee())
                .discountPerWeek(entity.getDiscountPerWeek())
                .images(imageList)

                .build();
    }

    @Override
    public CarDetailResponseForOwnerDTO toCarDetailResponseForOwnerDTO(Car entity) {
        List<ImageResponseDTO> imageList = entity.getImages().stream()
                .map(imageMapper::toImageResponseDTO)
                .toList();
        return CarDetailResponseForOwnerDTO.builder()
                .carId(entity.getId())
                .name(entity.getName())
                .licensePlate(entity.getLicensePlate())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .color(entity.getColor())
                .numberOfSeats(entity.getNumberOfSeats())
                .productionYear(entity.getProductionYear())
                .transmission(entity.getTransmission())
                .fuelType(entity.getFuelType())
                .createdAt(entity.getCreatedAt().toString())
                .updatedAt(entity.getUpdatedAt().toString())
                .rating(feedbackRepo.getRatingByCarId(entity.getId()))
                .completeBookingCount(bookingRepo.countCompletedBookingsByCarId(entity.getId()))
                .inProgressBookingCount(bookingRepo.countInProgressBookingsByCarId(entity.getId()))
                .pendingBookingCount(bookingRepo.countPendingBookingsByCarId(entity.getId()))
                .carStatus(entity.getStatus())
                .images(imageList)

                .mileage(entity.getMileage())
                .fuelConsumption(entity.getFuelConsumption())
                .address(entity.getAddress())
                .description(entity.getDescription())
                .additionalFunctions(entity.getAdditionalFunctions())
                .termOfUse(entity.getTermsOfUse())

                .basePrice(entity.getBasePrice().toBigInteger().toString())
                .quickRent(entity.getQuickRent())
                .maxDeliveryDistance(entity.getMaxDeliveryDistance())
                .deliveryFee(entity.getDeliveryFee())
                .freeDeliveryDistance(entity.getFreeDeliveryDistance())
                .kmPerDay(entity.getKmPerDay())
                .kmOverDayFee(entity.getKmOverDayFee())
                .discountPerWeek(entity.getDiscountPerWeek())

                .build();
    }

    @Override
    public Car addCarRequestDTOtoEntity(AddCarRequestDTO requestDTO) {

        return Car.builder()
                .name(requestDTO.name())
                .licensePlate(requestDTO.licensePlate())
//                .model(carModelRepo.findById(requestDTO.modelId()).orElse(null))
                .brand(requestDTO.brand())
                .model(requestDTO.model())
                .color(requestDTO.color())
                .numberOfSeats(requestDTO.numberOfSeats())
                .productionYear(requestDTO.productionYear())
                .transmission(requestDTO.transmission())
                .fuelType(requestDTO.fuelType())
                .mileage(requestDTO.mileage())
                .fuelConsumption(requestDTO.fuelConsumption())
                .address(requestDTO.address())
                .description(requestDTO.description())
                .additionalFunctions(requestDTO.additionalFunctions())
                .termsOfUse(requestDTO.termOfUse())
                .basePrice(new BigDecimal(requestDTO.basePrice()))
                .quickRent(requestDTO.quickRent())
                .maxDeliveryDistance(requestDTO.maxDeliveryDistance())
                .deliveryFee(requestDTO.deliveryFee())
                .freeDeliveryDistance(requestDTO.freeDeliveryDistance())
                .kmPerDay(requestDTO.kmPerDay())
                .kmOverDayFee(requestDTO.kmOverDayFee())
                .discountPerWeek(requestDTO.discountPerWeek())
                .status(ECarStatus.WAITING)
                .rating(null)
                .images(new ArrayList<>())
                .build();
    }

    @Override
    public Car registerRequestDTOtoEntity(Car oldCar, AddCarRequestDTO requestDTO) {

        oldCar.setName(requestDTO.name());
        oldCar.setLicensePlate(requestDTO.licensePlate());
        oldCar.setBrand(requestDTO.brand());
        oldCar.setModel(requestDTO.model());
        oldCar.setColor(requestDTO.color());
        oldCar.setNumberOfSeats(requestDTO.numberOfSeats());
        oldCar.setProductionYear(requestDTO.productionYear());
        oldCar.setTransmission(requestDTO.transmission());
        oldCar.setFuelType(requestDTO.fuelType());
        oldCar.setMileage(requestDTO.mileage());
        oldCar.setFuelConsumption(requestDTO.fuelConsumption());
        oldCar.setAddress(requestDTO.address());
        oldCar.setDescription(requestDTO.description());
        oldCar.setAdditionalFunctions(requestDTO.additionalFunctions());
        oldCar.setTermsOfUse(requestDTO.termOfUse());
        oldCar.setBasePrice(new BigDecimal(requestDTO.basePrice()));
        oldCar.setQuickRent(requestDTO.quickRent());
        oldCar.setMaxDeliveryDistance(requestDTO.maxDeliveryDistance());
        oldCar.setDeliveryFee(requestDTO.deliveryFee());
        oldCar.setFreeDeliveryDistance(requestDTO.freeDeliveryDistance());
        oldCar.setKmPerDay(requestDTO.kmPerDay());
        oldCar.setKmOverDayFee(requestDTO.kmOverDayFee());
        oldCar.setDiscountPerWeek(requestDTO.discountPerWeek());
        oldCar.setStatus(ECarStatus.WAITING);
        oldCar.setRating(null);
        oldCar.setImages(new ArrayList<>());

        return oldCar;
    }

    @Override
    public Car updCarRequestDTOtoEntity(Car oldCar, UpdCarRequestDTO requestDTO) {

        oldCar.setMileage(requestDTO.mileage());
        oldCar.setFuelConsumption(requestDTO.fuelConsumption());
        oldCar.setAddress(requestDTO.address());
        oldCar.setDescription(requestDTO.description());
        oldCar.setAdditionalFunctions(requestDTO.additionalFunctions());
        oldCar.setTermsOfUse(requestDTO.termOfUse());
        oldCar.setBasePrice(new BigDecimal(requestDTO.basePrice()));

        return oldCar;
    }

    @Override
    public Car updCarInfoRequestDTOtoEntity(Car oldCar, UpdCarInfoRequestDTO requestDTO) {

        oldCar.setMileage(requestDTO.mileage());
        oldCar.setFuelConsumption(requestDTO.fuelConsumption());
        oldCar.setAddress(requestDTO.address());
        oldCar.setDescription(requestDTO.description());
        oldCar.setAdditionalFunctions(requestDTO.additionalFunctions());
        oldCar.setTermsOfUse(requestDTO.termOfUse());
        oldCar.setUpdatedAt(LocalDate.now());

        return oldCar;
    }

    @Override
    public Car updCarPricingRequestDTOtoEntity(Car oldCar, UpdCarPricingRequestDTO requestDTO) {

        oldCar.setBasePrice(new BigDecimal(requestDTO.basePrice()));
        oldCar.setQuickRent(requestDTO.quickRent());
        oldCar.setMaxDeliveryDistance(requestDTO.maxDeliveryDistance());
        oldCar.setDeliveryFee(requestDTO.deliveryFee());
        oldCar.setFreeDeliveryDistance(requestDTO.freeDeliveryDistance());
        oldCar.setKmPerDay(requestDTO.kmPerDay());
        oldCar.setKmOverDayFee(requestDTO.kmOverDayFee());
        oldCar.setDiscountPerWeek(requestDTO.discountPerWeek());
        oldCar.setUpdatedAt(LocalDate.now());

        return oldCar;
    }
}
