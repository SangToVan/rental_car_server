package com.sangto.rental_car_server.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sangto.rental_car_server.domain.enums.EscrowStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "escrow_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EscrowTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "escrow_transaction_id")
    private Integer id;

    private Integer bookingId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private EscrowStatus status;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
