package com.sangto.rental_car_server.domain.dto.user;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public record UpdUserRequestDTO(
        String username,
        @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate birthday,
        String citizenId,
        @Pattern(regexp = "^0[0-9]{7,}$", message = "Phone number must start with 0 and contain at least 8 digits")
        String phoneNumber,
        String address
) {
}
