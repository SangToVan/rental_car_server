package com.sangto.rental_car_server.domain.entity;

import com.sangto.rental_car_server.domain.enums.EWalletType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

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

    @Enumerated(EnumType.STRING)
    private EWalletType walletType;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date updated_at;
}
