package com.sangto.rental_car_server.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sangto.rental_car_server.constant.TimeFormatConstant;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private EUserRole role;

    @Column(name = "birthday")
    @DateTimeFormat(pattern = TimeFormatConstant.DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeFormatConstant.DATE_FORMAT)
    private LocalDate birthday = LocalDate.now();

    private String citizenId;
    private String phoneNumber;
    private String address;
    private String drivingLicense;
    private String avatar;
    private String avatarPublicId;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDate createdAt = LocalDate.now();

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDate updatedAt = LocalDate.now();

    private boolean isActive;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @JsonIgnore
    @OneToMany(
            mappedBy = "user",
            targetEntity = Booking.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "user", targetEntity = Payment.class, fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(
            mappedBy = "carOwner",
            targetEntity = Car.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Car> cars = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
