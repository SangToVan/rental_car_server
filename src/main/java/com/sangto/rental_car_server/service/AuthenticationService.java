package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.auth.LoginRequestDTO;
import com.sangto.rental_car_server.domain.dto.auth.LoginResponseDTO;
import com.sangto.rental_car_server.responses.Response;

public interface AuthenticationService {
    Response<LoginResponseDTO> login(LoginRequestDTO requestDTO);
}
