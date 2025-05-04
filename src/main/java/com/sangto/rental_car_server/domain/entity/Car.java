package com.sangto.rental_car_server.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sangto.rental_car_server.domain.enums.ECarStatus;
import com.sangto.rental_car_server.domain.enums.ECarTransmission;
import com.sangto.rental_car_server.domain.enums.EFuelType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Integer id;

    private String name;

    @Column(nullable = false, unique = true)
    private String licensePlate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private CarModel model;

    private String color;
    private Integer numberOfSeats;
    private Integer productionYear;

    @Enumerated(EnumType.STRING)
    private ECarTransmission transmission;

    @Enumerated(EnumType.STRING)
    private EFuelType fuelType;

    private Integer mileage;
    private Float fuelConsumption;
    private String address;
    private String description;
    private String additionalFunctions;
    private String termsOfUse;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDate createdAt = LocalDate.now();

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDate updatedAt = LocalDate.now();

    @Column(nullable = false)
    private BigDecimal basePrice;

    @Enumerated(EnumType.STRING)
    private ECarStatus carStatus;

    private Double rating;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "car_owner_id", referencedColumnName = "user_id")
    private User carOwner;

    @JsonIgnore
    @OneToMany(
            mappedBy = "car",
            targetEntity = Booking.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Booking> booking = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            mappedBy = "car",
            targetEntity = Image.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    public void addImage(Image image) {
        images.add(image);
        image.setCar(this);
    }
}
