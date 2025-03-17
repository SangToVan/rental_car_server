package com.sangto.rental_car_server.utility;

import com.sangto.rental_car_server.domain.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    public static User getRequestedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
