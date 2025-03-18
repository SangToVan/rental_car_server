package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.auth.RegisterUserResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.AddUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UpdUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;

public interface UserMapper {
    RegisterUserResponseDTO toRegisterUserResponseDTO(User entity);

    UserDetailResponseDTO toUserDetailResponseDTO(User entity);

    UserResponseDTO toUserResponseDTO(User entity);

    User addUserRequestDTOtoEntity(AddUserRequestDTO requestDTO);

    User updUserRequestDTOtoEntity(User oldUser, UpdUserRequestDTO requestDTO);
}
