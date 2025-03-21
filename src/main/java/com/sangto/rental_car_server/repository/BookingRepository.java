package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b " + "JOIN FETCH b.car c " + "JOIN FETCH b.user u " + "WHERE u.id = :userId")
    List<Booking> getListBookingByUserId(@Param("userId") Integer userId);

    @Query("SELECT b FROM Booking b " + "JOIN FETCH b.car c " + "JOIN FETCH b.user u " + "WHERE c.id = :carId")
    List<Booking> getListBookingByCarId(@Param("carId") Integer carId);

    List<Booking> findAllByUserId(Integer userId);

}
