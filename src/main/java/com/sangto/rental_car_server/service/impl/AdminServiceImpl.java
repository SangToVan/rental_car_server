package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.admin.*;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import com.sangto.rental_car_server.domain.dto.car.CarDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.car.CarResponseDTO;
import com.sangto.rental_car_server.domain.dto.escrow_transaction.EscrowTransactionResponseDTO;
import com.sangto.rental_car_server.domain.dto.transaction.TransactionResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserResponseDTO;
import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.domain.enums.EVerifiedLicense;
import com.sangto.rental_car_server.domain.mapper.*;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.*;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepo;
    private final TransactionRepository transactionRepo;
    private final CarRepository carRepo;
    private final BookingRepository bookingRepo;
    private final EscrowTransactionRepository escrowTransactionRepo;

    private final TransactionMapper transactionMapper;
    private final CarMapper carMapper;
    private final BookingMapper bookingMapper;
    private final EscrowTransactionMapper escrowTransactionMapper;
    private final UserMapper userMapper;
    private final WalletMapper walletMapper;

    @Override
    public Response<DashboardResponseDTO> getDashboard() {
        Optional<User> findAdmin = userRepo.findAdmin(EUserRole.ADMIN);
        if (findAdmin.isEmpty()) throw new AppException("Admin not found");
        User admin = findAdmin.get();

        Optional<Wallet> findWallet = userRepo.findWalletById(admin.getId());
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet wallet = findWallet.get();

        List<TransactionResponseDTO> findList = transactionRepo.findRecentTransactionsByWallet(wallet.getId(),
                LocalDateTime.now().minusMonths(3L)).stream().map(transactionMapper::toTransactionResponseDTO).toList();

        return Response.successfulResponse("Get dashboard detail successfully",
                DashboardResponseDTO.builder()
                        .amount(wallet.getBalance().toBigInteger())
                        .list(findList)
                        .build());
    }

    @Override
    public Response<CarResponseForAdminDTO> getListCar(String status) {

        List<Car> verifiedCar = carRepo.findAllOtherStatusCars();
        List<Car> unverifiedCar = carRepo.findAllUnverifiedCars();
        List<Car> waitingCar = carRepo.findAllWaitingCars();
        List<CarResponseDTO> list;

        if (status.compareTo("UNVERIFIED") == 0) {
            list = unverifiedCar.stream().map(carMapper::toCarResponseDTO).toList();
        } else if (status.compareTo("WAITING") == 0) {
            list = waitingCar.stream().map(carMapper::toCarResponseDTO).toList();
        } else {
            list = verifiedCar.stream().map(carMapper::toCarResponseDTO).toList();
        }

        return Response.successfulResponse("Get list car successfully",
                CarResponseForAdminDTO.builder()
                        .verifiedCarCount(verifiedCar.size())
                        .unverifiedCarCount(unverifiedCar.size())
                        .waitingCarCount(waitingCar.size())
                        .list(list)
                        .build());
    }

    @Override
    public Response<CarDetailResponseDTO> getCarDetail(Integer carId) {
        Optional<Car> findCar = carRepo.findById(carId);
        if (findCar.isEmpty()) throw new AppException("This car is not existed");
        return Response.successfulResponse(
                "Get car detail successfully", carMapper.toCarDetailResponseDTO(findCar.get(), null)
        );
    }

    @Override
    public Response<BookingResponseForAdminDTO> getListBooking(String status) {
        List<Booking> finishedBooking = bookingRepo.getFinishedBookings();
        List<Booking> unfinishedBooking = bookingRepo.getUnfinishedBookings();
        List<Booking> allBookings = bookingRepo.getAllBookings();

        List<BookingResponseDTO> list;
        if (status.compareTo("FINISHED") == 0) {
            list = finishedBooking.stream().map(bookingMapper::toBookingResponseDTO).toList();
        } else if (status.compareTo("UNFINISHED") == 0) {
            list = unfinishedBooking.stream().map(bookingMapper::toBookingResponseDTO).toList();
        } else {
            list = allBookings.stream().map(bookingMapper::toBookingResponseDTO).toList();
        }
        return Response.successfulResponse("Get list booking successfully",
                BookingResponseForAdminDTO.builder()
                        .finishedBooking(finishedBooking.size())
                        .unfinishedBooking(unfinishedBooking.size())
                        .list(list)
                        .build());
    }

    @Override
    public Response<BookingDetailResponseDTO> getBookingDetail(Integer bookingId) {
        Optional<Booking> findBooking = bookingRepo.findById(bookingId);
        if (findBooking.isEmpty()) throw new AppException("This booking is not existed");

        return Response.successfulResponse("Get booking detail successfully",
                bookingMapper.toBookingDetailResponseDTO(findBooking.get()));
    }

    @Override
    public Response<EscrowResponseForAdminDTO> getListEscrow() {

        Optional<User> findSystem = userRepo.findAdmin(EUserRole.SYSTEM);
        if (findSystem.isEmpty()) throw new AppException("System not found");
        User system = findSystem.get();

        Optional<Wallet> findWallet = userRepo.findWalletById(system.getId());
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet wallet = findWallet.get();

        List<EscrowTransactionResponseDTO> list = escrowTransactionRepo.findAllOrderByCreatedAtDesc().stream().map(escrowTransactionMapper::toEscrowTransactionResponseDTO).toList();

        return Response.successfulResponse("Get list Escrow transaction successfully",
                EscrowResponseForAdminDTO.builder()
                        .totalEscrow(wallet.getBalance().toString())
                        .list(list)
                        .build());
    }

    @Override
    public Response<UserResponseForAdminDTO> getListUser(EUserRole userRole) {

        List<User> listCustomer = userRepo.findByRole(EUserRole.CUSTOMER);
        List<User> ListOwner = userRepo.findByRole(EUserRole.OWNER);
        List<User> listUser = userRepo.findByRoleNotIn(List.of(EUserRole.SYSTEM, EUserRole.ADMIN));


        List<UserResponseDTO> list;

        if(userRole.equals(EUserRole.CUSTOMER)) {
            list = listCustomer.stream().map(userMapper::toUserResponseDTO).toList();
        } else if (userRole.equals(EUserRole.OWNER)) {
            list = ListOwner.stream().map(userMapper::toUserResponseDTO).toList();
        } else {
            list = listUser.stream().map(userMapper::toUserResponseDTO).toList();
        }

        return Response.successfulResponse("Get list user successfully",
                UserResponseForAdminDTO.builder()
                        .customerCount(listCustomer.size())
                        .ownerCount(ListOwner.size())
                        .list(list)
                        .build());
    }

    @Override
    public Response<UserDetailResponseForAdminDTO> getUserDetail(Integer userId) {

        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("User not found");
        User user = findUser.get();

        Optional<Wallet> findWallet = userRepo.findWalletById(user.getId());
        if (findWallet.isEmpty()) throw new AppException("Wallet not found");
        Wallet wallet = findWallet.get();

        List<TransactionResponseDTO> findList = transactionRepo.findRecentTransactionsByWallet(wallet.getId(),
                LocalDateTime.now().minusMonths(3L)).stream().map(transactionMapper::toTransactionResponseDTO).toList();
        WalletResponseDTO walletDetail = WalletResponseDTO.builder()
                .balance(wallet.getBalance().toString())
                .transactionList(findList)
                .build();

        UserDetailResponseDTO userDetail = userMapper.toUserDetailResponseDTO(user);

        return Response.successfulResponse("Get user successfully",
                UserDetailResponseForAdminDTO.builder()
                        .userInfo(userDetail)
                        .wallet(walletDetail)
                        .build());
    }

    @Override
    public Response<String> verifyLicense(Integer userId) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("User not found");
        User user = findUser.get();

        if(user.getVerifiedLicense() == EVerifiedLicense.WAITING) {
            user.setVerifiedLicense(EVerifiedLicense.VERIFIED);
            userRepo.save(user);
        } else throw new AppException("Can not verify license for user");

        return Response.successfulResponse("Verified license successfully");
    }

    @Override
    public Response<String> unverifyLicense(Integer userId) {
        Optional<User> findUser = userRepo.findById(userId);
        if (findUser.isEmpty()) throw new AppException("User not found");
        User user = findUser.get();

        if(user.getVerifiedLicense() == EVerifiedLicense.WAITING) {
            user.setVerifiedLicense(EVerifiedLicense.UNVERIFIED);
            userRepo.save(user);
        } else throw new AppException("Can not unverified license for user");

        return Response.successfulResponse("Unverified license successfully");
    }
}
