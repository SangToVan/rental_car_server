package com.sangto.rental_car_server.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public record UpdUserRequestDTO(
        String username,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") LocalDate birthday,
        String gender,
        String citizenId,
        @Pattern(regexp = "^0[0-9]{7,}$", message = "Phone number must start with 0 and contain at least 8 digits")
        String phoneNumber,
        String email,
        String avatar,
        String drivingLicense,
        String licenseFullName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate licenseBirthday,
        String licenseImage

) {
}
