package com.sangto.rental_car_server.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sangto.rental_car_server.constant.TimeFormatConstant;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private int id;

    @DateTimeFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeFormatConstant.DATETIME_FORMAT)
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = TimeFormatConstant.DATETIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeFormatConstant.DATETIME_FORMAT)
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    private Long numberOfHours;

    @Enumerated(EnumType.STRING)
    private EPaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private EBookingStatus status;

    @ManyToOne(targetEntity = Car.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", referencedColumnName = "car_id")
    private Car car;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
