package com.sangto.rental_car_server.domain.dto.user;

import com.sangto.rental_car_server.domain.enums.EUserRole;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Builder
public record UserDetailResponseDTO(
        Integer userId,
        String username,
        String email,
        @Temporal(TemporalType.DATE) @DateTimeFormat(pattern = "yyyy/MM/dd") Date birthday,
        String citizenId,
        String phoneNumber,
        String address,
        boolean isActive,
        String balance,
        EUserRole role
) {
}
