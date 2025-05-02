package com.sangto.rental_car_server.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sangto.rental_car_server.domain.enums.EPaymentMethod;
import com.sangto.rental_car_server.domain.enums.EPaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private EPaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private EPaymentStatus paymentStatus;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime updatedAt = LocalDateTime.now();

    private Integer bookingId;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
