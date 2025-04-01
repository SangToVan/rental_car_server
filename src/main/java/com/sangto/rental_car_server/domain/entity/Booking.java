package com.sangto.rental_car_server.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sangto.rental_car_server.constant.TimeFormatConstant;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import com.sangto.rental_car_server.utility.RentalCalculateUtil;
import com.sangto.rental_car_server.utility.TimeUtil;
import jakarta.persistence.*;
import lombok.*;
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

    @Transient
    @Setter(AccessLevel.NONE)
    private BigDecimal rentalFee;

    public BigDecimal getRentalFee() {
        return RentalCalculateUtil.calculateRentalFee(this.startDateTime, this.endDateTime, this.car.getBasePrice());
    }

    @Transient
    @Setter(AccessLevel.NONE)
    private Long numberOfHours;

    public Long getNumberOfHours() {
        return RentalCalculateUtil.calculateHour(this.startDateTime, this.endDateTime);
    }

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
