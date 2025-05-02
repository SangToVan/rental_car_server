package com.sangto.rental_car_server.domain.dto.user;

import com.sangto.rental_car_server.domain.enums.EUserRole;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Builder
public record UserDetailResponseDTO(
        Integer id,
        String username,
        String email,
        EUserRole role,
        String birthday,
        String citizenId,
        String phoneNumber,
        String address,
        String drivingLicense,
        String avatar,
        String createdAt,
        String updatedAt,
        boolean isActive,
        String balance
) {
}
