package com.sangto.rental_car_server.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sangto.rental_car_server.domain.enums.ETransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallet_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_transaction_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ETransactionType transactionType;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @ManyToOne(targetEntity = Wallet.class)
    @JsonIgnore
    @JoinColumn(name = "wallet_id", referencedColumnName = "wallet_id")
    private Wallet wallet;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime transactionDate;
}
