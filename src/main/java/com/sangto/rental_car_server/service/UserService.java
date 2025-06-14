package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.auth.LoginResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.AddUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UpdUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserResponseDTO;
import com.sangto.rental_car_server.responses.Response;

import java.io.IOException;
import java.util.List;

public interface UserService {

    void initializeAdmin();

    void initializeSystem();

    Response<UserDetailResponseDTO> getDetailUser(Integer id);

    Response<LoginResponseDTO> addUser(AddUserRequestDTO requestDTO) throws IOException;

    Response<UserDetailResponseDTO> updateUser(Integer id, UpdUserRequestDTO requestDTO);

    Response<UserDetailResponseDTO> changeUserRole(Integer id);

    Response<List<UserResponseDTO>> getAllUsers();
}
