package com.sangto.rental_car_server.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.domain.enums.EVerifiedLicense;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserDetailResponseDTO(
        Integer userId,
        String username,
        String email,
        EUserRole role,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate birthday,
        String gender,
        String citizenId,
        String phoneNumber,
        String avatar,
        EVerifiedLicense verifiedLicense,
        String drivingLicense,
        String licenseFullName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate licenseBirthday,
        String licenseImage,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate createdAt,
        boolean isActive,
        String balance
) {
}
