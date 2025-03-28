package com.sangto.rental_car_server.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "system_wallets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "system_wallet_id")
    private Integer id;

    private BigDecimal serviceFee = BigDecimal.ZERO;
    private BigDecimal rentalFee = BigDecimal.ZERO;
    private BigDecimal reserve = BigDecimal.ZERO;

    public BigDecimal getTotalBalance() {
        return serviceFee.add(rentalFee).add(reserve);
    }

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "systemWallet",
            targetEntity = WalletTransaction.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<WalletTransaction> transactions;
}
