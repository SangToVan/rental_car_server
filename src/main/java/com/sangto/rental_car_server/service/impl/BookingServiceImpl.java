package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.constant.MailTemplate;
import com.sangto.rental_car_server.constant.MetaConstant;
import com.sangto.rental_car_server.constant.TimeFormatConstant;
import com.sangto.rental_car_server.domain.dto.booking.*;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.SortingDTO;
import com.sangto.rental_car_server.domain.dto.payment.AddPaymentRequestDTO;
import com.sangto.rental_car_server.domain.dto.payment.PaymentResponseDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.*;
import com.sangto.rental_car_server.domain.mapper.BookingMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.BookingRepository;
import com.sangto.rental_car_server.repository.CarRepository;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.responses.MetaResponse;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.BookingService;
import com.sangto.rental_car_server.service.CarService;
import com.sangto.rental_car_server.service.EscrowTransactionService;
import com.sangto.rental_car_server.service.PaymentService;
import com.sangto.rental_car_server.utility.MailSenderUtil;
import com.sangto.rental_car_server.utility.RentalCalculateUtil;
import com.sangto.rental_car_server.utility.TimeUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class BookingServiceImpl implements BookingService {

    private final CarRepository carRepo;
    private final UserRepository userRepo;
    private final BookingRepository bookingRepo;
    private final CarService carService;
    private final PaymentService paymentService;
    private final EscrowTransactionService escrowTransactionService;
    private final BookingMapper bookingMapper;
    private final MailSenderUtil mailSenderUtil;

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
    public MetaResponse<MetaResponseDTO, List<BookingResponseDTO>> getAllBookings(MetaRequestDTO metaRequestDTO) {
        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(metaRequestDTO.sortField()).ascending()
                : Sort.by(metaRequestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);
        Page<Booking> page = bookingRepo.findAll(pageable);
        if (page.getContent().isEmpty()) throw new AppException("List booking is empty");
        List<BookingResponseDTO> li = page.getContent().stream()
                .map(bookingMapper::toBookingResponseDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get all booking successfully",
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
                li
        );
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<BookingResponseDTO>> getAllBookingForUser(MetaRequestDTO metaRequestDTO, Integer userId) {

        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("This user is not existed");

        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(metaRequestDTO.sortField()).ascending()
                : Sort.by(metaRequestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);
        Page<Booking> page = metaRequestDTO.keyword() == null
                ? bookingRepo.getListBookingByUserId(userId, pageable)
                : bookingRepo.getListBookingByUserIdWithKeyword(userId, metaRequestDTO.keyword(), pageable);
        if (page.getContent().isEmpty()) throw new AppException("List booking is empty");
        List<BookingResponseDTO> li = page.getContent().stream()
                .map(bookingMapper::toBookingResponseDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get all booking for user successfully",
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
                li
        );
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<BookingResponseDTO>> getUnfinishedBookingForUser(MetaRequestDTO metaRequestDTO, Integer userId) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("This user is not existed");

        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(metaRequestDTO.sortField()).ascending()
                : Sort.by(metaRequestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);
        Page<Booking> page = bookingRepo.getUnfinishedBookingsByUserId(userId, pageable);

        if (page.getContent().isEmpty()) throw new AppException("List booking is empty");
        List<BookingResponseDTO> li = page.getContent().stream()
                .map(bookingMapper::toBookingResponseDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get list unfinished booking for user successfully",
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
                li
        );
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<BookingResponseDTO>> getFinishedBookingForUser(MetaRequestDTO metaRequestDTO, Integer userId) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("This user is not existed");

        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(metaRequestDTO.sortField()).ascending()
                : Sort.by(metaRequestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);
        Page<Booking> page = bookingRepo.getFinishedBookingsByUserId(userId, pageable);

        if (page.getContent().isEmpty()) throw new AppException("List booking is empty");
        List<BookingResponseDTO> li = page.getContent().stream()
                .map(bookingMapper::toBookingResponseDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get list finished booking for user successfully",
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
                li
        );
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<BookingResponseForOwnerDTO>> getAllBookingForCar(MetaRequestDTO metaRequestDTO, Integer carId, Integer ownerId) {
        carService.verifyCarOwner(ownerId, carId);

        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(metaRequestDTO.sortField()).ascending()
                : Sort.by(metaRequestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);
        Page<Booking> page = metaRequestDTO.keyword() == null
                ? bookingRepo.getListBookingByCarId(carId, pageable)
                : bookingRepo.getListBookingByUserId(carId, pageable);
        if (page.getContent().isEmpty()) throw new AppException("List booking is empty");
        List<BookingResponseForOwnerDTO> li = page.getContent().stream()
                .map(bookingMapper::toBookingResponseForOwnerDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get all booking successfully",
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
                li
        );
    }

    @Override
    public MetaResponse<MetaResponseDTO, List<BookingResponseForOwnerDTO>> getAllBookingForCarByStatus(MetaRequestDTO metaRequestDTO, Integer carId, Integer ownerId, EBookingStatus status) {
        carService.verifyCarOwner(ownerId, carId);

        Sort sort = metaRequestDTO.sortDir().equals(MetaConstant.Sorting.DEFAULT_DIRECTION)
                ? Sort.by(metaRequestDTO.sortField()).ascending()
                : Sort.by(metaRequestDTO.sortField()).descending();
        Pageable pageable = PageRequest.of(metaRequestDTO.currentPage(), metaRequestDTO.pageSize(), sort);
        Page<Booking> page = null;
        if(status == null) {
            page = bookingRepo.getListBookingByCarId(carId, pageable);
        } else {
            page = bookingRepo.getListBookingByCarIdAndStatusOrderByBookingDateDesc(carId, status, pageable);
        }
        if (page.getContent().isEmpty()) throw new AppException("List booking is empty");
        List<BookingResponseForOwnerDTO> li = page.getContent().stream()
                .map(bookingMapper::toBookingResponseForOwnerDTO)
                .toList();

        return MetaResponse.successfulResponse(
                "Get all booking successfully",
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
                li
        );
    }

    @Override
    public Response<BookingStatisticsResponseDTO> getBookingStatistics(Integer ownerId, LocalDateTime startDate, LocalDateTime endDate) {

        List<BookingResponseForOwnerDTO> listResponse = null;
        Integer countComplete = 0;
        Integer countCancelled = 0;
        BigDecimal revenue = BigDecimal.ZERO;
        if (startDate != null && endDate != null) {
            revenue = bookingRepo.getTotalRevenueByOwnerAndTime(ownerId, startDate, endDate);
            countComplete = bookingRepo.countBookingsByStatusAndOwnerAndTime(ownerId, EBookingStatus.COMPLETED, startDate, endDate);
            countCancelled = bookingRepo.countBookingsByStatusAndOwnerAndTime(ownerId, EBookingStatus.CANCELLED, startDate, endDate);
            listResponse = bookingRepo.findBookingsByOwnerAndStatus(ownerId, startDate, endDate, EBookingStatus.COMPLETED).stream().map(bookingMapper::toBookingResponseForOwnerDTO).toList();
        } else {
            revenue = bookingRepo.getTotalRevenueByOwner(ownerId);
            countComplete = bookingRepo.countBookingsByStatusAndOwner(ownerId, EBookingStatus.COMPLETED);
            countCancelled = bookingRepo.countBookingsByStatusAndOwner(ownerId, EBookingStatus.CANCELLED);
            listResponse = bookingRepo.findAllBookingsByOwnerAndStatus(ownerId, EBookingStatus.COMPLETED).stream().map(bookingMapper::toBookingResponseForOwnerDTO).toList();
        }

        return Response.successfulResponse("Get booking statistics successfully",
                BookingStatisticsResponseDTO.builder()
                        .revenue(revenue.toString())
                        .countCancelled(countCancelled)
                        .countCompleted(countComplete)
                        .list(listResponse)
                        .build());
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
    public Response<BookingDetailResponseDTO> addBooking(Integer userId, AddBookingRequestDTO requestDTO) throws MessagingException {
        // find car
        Optional<Car> findCar = carRepo.findById(requestDTO.carId());
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        Car car = findCar.get();

        // check car status
        if(car.getStatus() == ECarStatus.UNVERIFIED) {
            throw new AppException("This car is unverified");
        }
        if (car.getStatus() == ECarStatus.SUSPENDED) {
            throw new AppException("This car is suspended");
        }

        // Check schedule to rent car
        Optional<Car> checkScheduleCar =
                carRepo.checkScheduleCar(requestDTO.carId(), requestDTO.startDateTime(), requestDTO.endDateTime());
        if (checkScheduleCar.isEmpty()) throw new AppException("Invalid booking schedule. Please try again.");
        // find customer
        Optional<User> findCustomer = userRepo.findById(userId);
        if (findCustomer.isEmpty()) throw new AppException("This user is not existed");
        User customer = findCustomer.get();

        if (customer.getId().equals(car.getCarOwner().getId())) throw new AppException("Owner can not rent car by yourself");

        // Calculate rental duration and rental fee
        // Convert startDateTime and endDateTime from String to Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeFormatConstant.DATETIME_FORMAT);
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        try {
            startDateTime = LocalDateTime.parse(requestDTO.startDateTime(), formatter);
            endDateTime = LocalDateTime.parse(requestDTO.endDateTime(), formatter);
        } catch (DateTimeParseException e) {
            throw new AppException("Invalid date format ", e);
        } catch (NullPointerException ex) {
            throw new AppException("Date time not null ", ex);
        }

        // check time rent
        Long rentalDurationHours = RentalCalculateUtil.calculateHour(startDateTime, endDateTime);
        if (rentalDurationHours <= 0) throw new AppException("Rental duration less than 0");

        // calculate rental cost
        BigDecimal totalRentalCost = RentalCalculateUtil.calculateTotalFee(startDateTime, endDateTime, car.getBasePrice());
        // Booking
        Booking newBooking = bookingMapper.addBookingRequestDTOtoEntity(requestDTO);
        newBooking.setCar(car);
        newBooking.setUser(customer);

        newBooking.setTotalPrice(totalRentalCost);
        newBooking.setStatus(EBookingStatus.PENDING);
        newBooking.setDepositAmount(BigDecimal.ZERO);
        newBooking.setTotalPaidAmount(BigDecimal.ZERO);
        newBooking.setNeedToPayInCash(BigDecimal.ZERO);
        newBooking.setRefundAmount(BigDecimal.ZERO);
        newBooking.setPayoutAmount(BigDecimal.ZERO);
        newBooking.setRefunded(false);
        newBooking.setPayoutDone(false);

        Booking saveBooking = bookingRepo.save(newBooking);

        // Send Mail To Owner
        String toMail = customer.getEmail();
        String subject = MailTemplate.RENT_A_CAR.RENT_A_CAR_SUBJECT;
        String template = MailTemplate.RENT_A_CAR.RENT_A_CAR_TEMPLATE;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter mailFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String bookingTime = now.format(mailFormat);
        Map<String, Object> variable = Map.of(
                "carName", car.getName(),
                "bookingTime", bookingTime,
                "bookingId", saveBooking.getId());
        mailSenderUtil.sendMailWithHTML(toMail, subject, template, variable);

        return Response.successfulResponse(
                "Add new booking successfully",
                bookingMapper.toBookingDetailResponseDTO(saveBooking)
        );
    }

    @Override
    @Transactional
    public Response<PaymentResponseDTO> paymentBooking(Integer bookingId, Integer userId, AddPaymentRequestDTO requestDTO, HttpServletRequest request) {
        Booking booking = this.verifyBookingCustomer(userId, bookingId);
        if (booking.getStatus() == EBookingStatus.PENDING) {

            PaymentResponseDTO responseDTO = paymentService.addPayment(booking.getId(), requestDTO, request);
            booking.setPaymentMethod(requestDTO.paymentMethod());

            if (booking.getTotalPaidAmount().compareTo(booking.getTotalPrice()) >= 0) {
                booking.setStatus(EBookingStatus.PAID);
            }
            bookingRepo.save(booking);
            return Response.successfulResponse(
                    "Payment booking successfully", responseDTO
            );
        } else {
            throw new AppException("Cannot payment booking");
        }
    }

    @Override
    public Response<String> confirmBooking(Integer bookingId, Integer userId) throws MessagingException {
        Booking booking = this.verifyBookingCarOwner(userId, bookingId);
        if (booking.getStatus() == EBookingStatus.PAID) {
            booking.setStatus(EBookingStatus.CONFIRMED);
            bookingRepo.save(booking);

            User customer = booking.getUser();
            Car car = booking.getCar();

            // Send Mail To Customer
            String toMail = customer.getEmail();
            String subject = MailTemplate.CONFIRM_DEPOSIT.CONFIRM_DEPOSIT_SUBJECT;
            String template = MailTemplate.CONFIRM_DEPOSIT.CONFIRM_DEPOSIT_TEMPLATE;
            Map<String, Object> variable = Map.of(
                    "carName", car.getName(),
                    "startTime", TimeUtil.formatToString(booking.getStartDateTime()),
                    "endTime", TimeUtil.formatToString(booking.getEndDateTime()));
            mailSenderUtil.sendMailWithHTML(toMail, subject, template, variable);

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
            booking.setStatus(EBookingStatus.RETURNED);
            bookingRepo.save(booking);
            return Response.successfulResponse(
                    "Confirm return successfully"
            );
        } else {
            throw new AppException("Cannot confirm return");
        }
    }

    @Override
    @Transactional
    public Response<String> completeBooking(Integer bookingId) {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("This booking is not existed");
        Booking booking = findBooking.get();
        if (booking.getStatus() == EBookingStatus.RETURNED) {

            try {
                if (!booking.isPayoutDone()) {
                    paymentService.releasePayment(booking.getId());
                }
            } catch (AppException e) {
                throw new AppException(e.getMessage(), e.getCause());
            }

            booking.setStatus(EBookingStatus.COMPLETED);
            bookingRepo.save(booking);
            return Response.successfulResponse(
                    "Complete booking successfully"
            );
        } else {
            throw new AppException("Cannot complete booking");
        }
    }

    @Override
    @Transactional
    public Response<String> cancelBooking(Integer bookingId, Integer userId) throws MessagingException {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("This booking is not existed");

        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("This user is not existed");

        Booking booking = findBooking.get();
        User user = findUser.get();

        if(user.getRole() == EUserRole.ADMIN) {
            return adminCancelBooking(booking.getId());
        } else {
            if (Objects.equals(booking.getUser().getId(), user.getId())) {
                return customerCancelBooking(booking.getId(), user.getId());
            } else if (Objects.equals(booking.getCar().getCarOwner().getId(), user.getId())) {
                return ownerCancelBooking(booking.getId(), user.getId());
            }
        }

        throw new AppException("Cannot cancel booking");
    }

    @Override
    @Transactional
    public Response<String> customerCancelBooking(Integer bookingId, Integer userId) throws MessagingException {
        Booking booking = this.verifyBookingCustomer(userId, bookingId);

        User customer = booking.getUser();
        User owner = booking.getCar().getCarOwner();
        Car car = booking.getCar();

        if (booking.getStatus() == EBookingStatus.PENDING) {
            booking.setStatus(EBookingStatus.CANCELLED);
            bookingRepo.save(booking);

            // booking is paid or confirmed
        } else if (booking.getStatus() == EBookingStatus.CONFIRMED || booking.getStatus() == EBookingStatus.PAID) {
            paymentService.refundPayment(booking.getId());
            booking.setStatus(EBookingStatus.CANCELLED);
            bookingRepo.save(booking);

        } else {
            throw new AppException("Booking is in progress, cannot cancel");
        }

        // Send Mail To Owner
        String toMail = owner.getEmail();
        String subject = MailTemplate.CANCEL_BOOKING.CUSTOMER_CANCEL_BOOKING_SUBJECT;
        String template = MailTemplate.CANCEL_BOOKING.CANCEL_BOOKING_TEMPLATE;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String cancelTime = now.format(formatter).toString();
        Map<String, Object> variable = Map.of("carName", car.getName(), "cancelTime", cancelTime);
        mailSenderUtil.sendMailWithHTML(toMail, subject, template, variable);

        return Response.successfulResponse(
                "Confirm cancelled successfully"
        );
    }

    @Override
    @Transactional
    public Response<String> ownerCancelBooking(Integer bookingId, Integer userId) throws MessagingException {
        Booking booking = this.verifyBookingCarOwner(userId, bookingId);

        User customer = booking.getUser();
        User owner = booking.getCar().getCarOwner();
        Car car = booking.getCar();

        if (booking.getStatus() == EBookingStatus.PENDING) {
            booking.setStatus(EBookingStatus.CANCELLED);
            bookingRepo.save(booking);

            // booking is paid or confirmed
        } else if (booking.getStatus() == EBookingStatus.CONFIRMED || booking.getStatus() == EBookingStatus.PAID) {
            paymentService.refundPayment(booking.getId());
            booking.setStatus(EBookingStatus.CANCELLED);
            bookingRepo.save(booking);

        } else {
            throw new AppException("Booking is in progress, cannot cancel");
        }

        // Send Mail To Customer
        String toMail = customer.getEmail();
        String subject = MailTemplate.CANCEL_BOOKING.OWNER_CANCEL_BOOKING_SUBJECT;
        String template = MailTemplate.CANCEL_BOOKING.CANCEL_BOOKING_TEMPLATE;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String cancelTime = now.format(formatter).toString();
        Map<String, Object> variable = Map.of("carName", car.getName(), "cancelTime", cancelTime);
        mailSenderUtil.sendMailWithHTML(toMail, subject, template, variable);

        return Response.successfulResponse(
                "Confirm cancelled successfully"
        );
    }

    @Override
    @Transactional
    public Response<String> adminCancelBooking(Integer bookingId) throws MessagingException {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("This booking is not existed");
        Booking booking = findBooking.get();

        User customer = booking.getUser();
        User owner = booking.getCar().getCarOwner();
        Car car = booking.getCar();

        if (booking.getStatus() == EBookingStatus.PENDING) {
            booking.setStatus(EBookingStatus.CANCELLED);
            bookingRepo.save(booking);

            // booking is paid or confirmed
        } else if (booking.getStatus() == EBookingStatus.CONFIRMED || booking.getStatus() == EBookingStatus.PAID) {
            paymentService.refundPayment(booking.getId());
            booking.setStatus(EBookingStatus.CANCELLED);
            bookingRepo.save(booking);

        } else {
            throw new AppException("Booking is in progress, cannot cancel");
        }

        // Send mail
        String toMailCustomer = customer.getEmail();
        String toMailOwner = owner.getEmail();

        String subject = MailTemplate.CANCEL_BOOKING.ADMIN_CANCEL_BOOKING_SUBJECT;
        String template = MailTemplate.CANCEL_BOOKING.CANCEL_BOOKING_TEMPLATE;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String cancelTime = now.format(formatter).toString();
        Map<String, Object> variable = Map.of("carName", car.getName(), "cancelTime", cancelTime);

        mailSenderUtil.sendMailWithHTML(toMailCustomer, subject, template, variable);
        mailSenderUtil.sendMailWithHTML(toMailOwner, subject, template, variable);

        return Response.successfulResponse(
                "Confirm cancelled successfully"
        );
    }
}
