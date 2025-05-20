package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Booking;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b " + "JOIN FETCH b.car c " + "JOIN FETCH b.user u " + "WHERE u.id = :userId")
    Page<Booking> getListBookingByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.car c " +
            "JOIN FETCH b.user u " +
            "WHERE u.id = :userId " +
            "AND (b.status = 'PENDING' OR b.status = 'PAID' OR b.status = 'CONFIRMED' OR b.status = 'IN_PROGRESS')")
    Page<Booking> getUnfinishedBookingsByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.car c " +
            "JOIN FETCH b.user u " +
            "ORDER BY b.bookingDate DESC")
    List<Booking> getAllBookings();

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.car c " +
            "JOIN FETCH b.user u " +
            "WHERE b.status = 'PENDING' OR b.status = 'PAID' OR b.status = 'CONFIRMED' OR b.status = 'IN_PROGRESS' " +
            "ORDER BY b.bookingDate DESC")
    List<Booking> getUnfinishedBookings();

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.car c " +
            "JOIN FETCH b.user u " +
            "WHERE u.id = :userId " +
            "AND (b.status = 'COMPLETED' OR b.status = 'RETURNED' OR b.status = 'CANCELLED')")
    Page<Booking> getFinishedBookingsByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.car c " +
            "JOIN FETCH b.user u " +
            "WHERE b.status = 'COMPLETED' OR b.status = 'RETURNED' OR b.status = 'CANCELLED' " +
            "ORDER BY b.bookingDate DESC")
    List<Booking> getFinishedBookings();

    @Query("SELECT b FROM Booking b " + "JOIN FETCH b.car c " + "JOIN FETCH b.user u " + "WHERE c.id = :carId")
    Page<Booking> getListBookingByCarId(@Param("carId") Integer carId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.car.id = :carId " +
            "AND b.status = :status " +
            "ORDER BY b.bookingDate DESC")
    Page<Booking> getListBookingByCarIdAndStatusOrderByBookingDateDesc(
            @Param("carId") Integer carId,
            @Param("status") EBookingStatus status, Pageable pageable);

    @Query("SELECT b FROM Booking b " + "JOIN FETCH b.car c "
            + "JOIN FETCH b.user u "
            + "WHERE u.id = :userId AND CONCAT(b.status,' ',b.paymentMethod) LIKE %:keyword%")
    Page<Booking> getListBookingByUserIdWithKeyword(
            @Param("userId") Integer userId, @Param("keyword") String keyword, Pageable pageable);

    List<Booking> findAllByUserId(Integer userId);

    @Query("SELECT b FROM Booking b WHERE b.status = :status AND b.startDateTime < :overdueTime")
    List<Booking> findByStatusAndStartDateTime(@Param("status") EBookingStatus status, @Param("overdueTime") LocalDateTime overdueTime);

    @Query("SELECT b FROM Booking b WHERE b.status = :status AND b.endDateTime > :reminderTime")
    List<Booking> findByStatusAndEndDateTime(@Param("status") EBookingStatus status, @Param("reminderTime") LocalDateTime reminderTime);
    @Query("SELECT b FROM Booking b WHERE b.car.carOwner.id = :ownerId AND b.status = :status")
    List<Booking> findByCarOwnerIdAndBookingStatus(@Param("ownerId") Integer ownerId, @Param("status") EBookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.car.id = :carId AND b.startDateTime <= :now AND b.endDateTime > :now AND b.status = 'CONFIRMED'")
    List<Booking> findActiveBookingsByCarId(@Param("carId") Integer carId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.car.id = :carId AND b.startDateTime > :now AND b.status = 'CONFIRMED'")
    List<Booking> findFutureBookingsByCarId(@Param("carId") Integer carId, @Param("now") LocalDateTime now);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.startDateTime BETWEEN :startOfMonth AND :endOfMonth")
    int countBookingsInMonth(@Param("startOfMonth") LocalDateTime startOfMonth, @Param("endOfMonth") LocalDateTime endOfMonth);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.car.id = :carId AND b.status = 'COMPLETED'")
    int countCompletedBookingsByCarId(@Param("carId") Integer carId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.car.id = :carId AND (b.status = 'PENDING' OR b.status = 'PAID')")
    int countPendingBookingsByCarId(@Param("carId") Integer carId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.car.id = :carId AND (b.status = 'CONFIRMED' OR b.status = 'IN_PROGRESS' OR b.status = 'RETURNED')")
    int countInProgressBookingsByCarId(@Param("carId") Integer carId);

}
