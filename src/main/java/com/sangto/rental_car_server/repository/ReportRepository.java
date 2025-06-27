package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    List<Report> findReportByBookingId(Integer bookingId);

    @Query("SELECT r FROM Report r ORDER BY r.createdAt DESC")
    List<Report> getAllOrderByCreatedAtDesc();
}
