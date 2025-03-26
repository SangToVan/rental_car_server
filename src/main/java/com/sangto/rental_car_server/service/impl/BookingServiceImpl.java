package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.constant.TimeFormatConstant;
import com.sangto.rental_car_server.domain.dto.booking.AddBookingRequestDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseForOwnerDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import com.sangto.rental_car_server.domain.mapper.BookingMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.BookingRepository;
import com.sangto.rental_car_server.repository.CarRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.BookingService;
import com.sangto.rental_car_server.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class BookingServiceImpl implements BookingService {

    private final CarRepository carRepo;
    private final UserRepository userRepo;
    private final BookingRepository bookingRepo;
    private final CarService carService;
    private final BookingMapper bookingMapper;

    @Override
    public Booking verifyBookingCarOwner(Integer ownerId, Integer bookingId) {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("This booking is not existed");
        Booking booking = findBooking.get();
        if (booking.getCar().getCarOwner().getId() != ownerId) throw new AppException("Unauthorized");
        return booking;
    }

    @Override
    public Booking verifyBookingCustomer(Integer customerId, Integer bookingId) {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("This booking is not existed");
        Booking booking = findBooking.get();
        if (booking.getUser().getId() != customerId) throw new AppException("Unauthorized");
        return booking;
    }

    @Override
    public Response<List<BookingResponseDTO>> getAllBookings() {
        List<Booking> bookings = bookingRepo.findAll();
        List<BookingResponseDTO> li = bookings.stream()
                .map(bookingMapper::toBookingResponseDTO).toList();
        return Response.successfulResponse(
                "Get all booking successfully", li
        );
    }

    @Override
    public Response<List<BookingResponseDTO>> getAllBookingForUser(Integer userId) {
        List<Booking> bookings = bookingRepo.getListBookingByUserId(userId);
        List<BookingResponseDTO> li = bookings.stream()
                .map(bookingMapper::toBookingResponseDTO).toList();
        return Response.successfulResponse(
                "Get all booking for user successfully", li
        );
    }

    @Override
    public Response<List<BookingResponseForOwnerDTO>> getAllBookingForCar(Integer carId) {
        List<Booking> bookings = bookingRepo.getListBookingByCarId(carId);
        List<BookingResponseForOwnerDTO> li = bookings.stream()
                .map(bookingMapper::toBookingResponseForOwnerDTO).toList();
        return Response.successfulResponse(
                "Get all booking successfully", li
        );
    }

    @Override
    public Response<BookingDetailResponseDTO> getBookingDetail(Integer bookingId) {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("This booking is not existed");

        return Response.successfulResponse(
                "Get detail booking successful",
                bookingMapper.toBookingDetailResponseDTO(findBooking.get()));
    }

    @Override
    public Response<BookingDetailResponseDTO> addBooking(Integer userId, AddBookingRequestDTO requestDTO) {
        // find car
        Optional<Car> findCar = carRepo.findById(requestDTO.carId());
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        Car car = findCar.get();

        // check car status
        if(car.getCarStatus() == ECarStatus.UNVERIFIED) {
            throw new AppException("This car is unverified");
        }
        if (car.getCarStatus() == ECarStatus.SUSPENDED) {
            throw new AppException("This car is suspended");
        }

        // check time rent

        // find customer
        Optional<User> findCustomer = userRepo.findById(userId);
        if (findCustomer.isEmpty()) throw new AppException("This user is not existed");
        User customer = findCustomer.get();

        // Convert startDateTime and endDateTime from String to Date
        SimpleDateFormat sdf = new SimpleDateFormat(TimeFormatConstant.DATETIME_FORMAT);
        Date startDateTime = null;
        Date endDateTime = null;
        try {
            startDateTime = sdf.parse(requestDTO.startDateTime());
            endDateTime = sdf.parse(requestDTO.endDateTime());
        } catch (ParseException e) {
            throw new AppException("Invalid date format", e);
        }

        // Calculate rental duration and rental fee

        // Booking
        Booking newBooking = bookingMapper.addBookingRequestDTOtoEntity(requestDTO);
        newBooking.setCar(car);
        newBooking.setUser(customer);
        return Response.successfulResponse(
                "Add new booking successfully",
                bookingMapper.toBookingDetailResponseDTO(newBooking)
        );
    }

    @Override
    public Response<String> confirmBooking(Integer bookingId, Integer userId) {
        Booking booking = this.verifyBookingCarOwner(userId, bookingId);
        if (booking.getStatus() == EBookingStatus.PENDING) {
            booking.setStatus(EBookingStatus.CONFIRMED);
            bookingRepo.save(booking);
            return Response.successfulResponse(
                    "Confirm booking successfully"
            );
        } else {
            throw new AppException("Cannot confirm booking");
        }
    }

    @Override
    public Response<String> confirmPickup(Integer bookingId, Integer userId) {
        Booking booking = this.verifyBookingCustomer(userId, bookingId);
        if (booking.getStatus() == EBookingStatus.CONFIRMED) {
            booking.setStatus(EBookingStatus.IN_PROGRESS);
            bookingRepo.save(booking);
            return Response.successfulResponse(
                    "Confirm pickup successfully"
            );
        } else {
            throw new AppException("Cannot confirm pickup");
        }
    }

    @Override
    public Response<String> confirmReturn(Integer bookingId, Integer userId) {
        Booking booking = this.verifyBookingCarOwner(userId, bookingId);
        if (booking.getStatus() == EBookingStatus.IN_PROGRESS) {
            booking.setStatus(EBookingStatus.COMPLETED);
            bookingRepo.save(booking);
            return Response.successfulResponse(
                    "Confirm return successfully"
            );
        } else {
            throw new AppException("Cannot confirm return");
        }
    }

    @Override
    public Response<String> cancelBooking(Integer bookingId, Integer userId) {
        return null;
    }
}
