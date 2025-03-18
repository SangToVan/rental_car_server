package com.sangto.rental_car_server.domain.dto.auth;

import com.sangto.rental_car_server.annotation.StrongPassword;

public record ChangePasswordRequestDTO(
        @StrongPassword
        String newPassword,
        String oldPassword
) {
}
