package com.sangto.rental_car_server.domain.entity;

import com.sangto.rental_car_server.domain.enums.ECarType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "car_models")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_id")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private ECarType carType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private CarBrand brand;
}
