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
        String email,
        @Pattern(regexp = "^0[0-9]{7,}$", message = "Phone number must start with 0 and contain at least 8 digits")
        String phoneNumber
) {
}
