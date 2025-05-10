package com.sangto.rental_car_server.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sangto.rental_car_server.constant.TimeFormatConstant;
import com.sangto.rental_car_server.domain.enums.EBookingStatus;
import com.sangto.rental_car_server.domain.enums.EDocumentRental;
import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import com.sangto.rental_car_server.domain.enums.ERelationship;
import com.sangto.rental_car_server.utility.RentalCalculateUtil;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal totalPrice;

    @Transient
    @Setter(AccessLevel.NONE)
    private BigDecimal rentalFee;

    public BigDecimal getRentalFee() {
        return RentalCalculateUtil.calculateRentalFee(this.startDateTime, this.endDateTime, this.car.getBasePrice());
    }

    @Transient
    @Setter(AccessLevel.NONE)
    private Long numberOfDays;

    public Long getNumberOfDays() {
        return RentalCalculateUtil.calculateRentalDays(this.startDateTime, this.endDateTime);
    }

    @Enumerated(EnumType.STRING)
    private EPaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private EBookingStatus status;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private EDocumentRental documentRental = EDocumentRental.NATIVE;

    private String driverName;
    private String driverPhone;
    private String driverCitizenId;

    @Enumerated(EnumType.STRING)
    private ERelationship relationship;

    // ✅ Các trường liên quan đến thanh toán
    // số tiền cần thanh toán
    @Column(precision = 18, scale = 2)
    private BigDecimal depositAmount;

    // số tiền đã thanh toán
    @Column(precision = 18, scale = 2)
    private BigDecimal totalPaidAmount;

    // số tiền cần thanh toán khi nhận xe
    @Column(precision = 18, scale = 2)
    private BigDecimal needToPayInCash;

    // số tiền cần hoàn trả
    @Column(precision = 18, scale = 2)
    private BigDecimal refundAmount;

    // số tiền sẽ trả cho chủ xe
    @Column(precision = 18, scale = 2)
    private BigDecimal payoutAmount;

    // kiểm tra hoàn tiền cho khách hàng
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isRefunded;

    // kiểm tra thanh toán cho chủ xe
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isPayoutDone;

    // ✅ Quan hệ với các thực thể khác
    @OneToMany(mappedBy = "booking", targetEntity = Payment.class, fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    @ManyToOne(targetEntity = Car.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", referencedColumnName = "car_id")
    private Car car;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "booking", orphanRemoval = true)
    private Feedback feedback;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private EscrowTransaction escrowTransaction;
}
