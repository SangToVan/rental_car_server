package com.sangto.rental_car_server.domain.dto.user;

import com.sangto.rental_car_server.annotation.EnumValid;
import com.sangto.rental_car_server.annotation.PasswordMatches;
import com.sangto.rental_car_server.annotation.StrongPassword;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import jakarta.validation.constraints.Pattern;

@PasswordMatches
public record AddUserRequestDTO(
        @StrongPassword
        String password,
        String confirmPassword,
        String username,
        String email
) {
}
