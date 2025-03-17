package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.auth.RegisterUserResponseDTO;
import com.sangto.rental_car_server.domain.mapper.UserMapper;
import com.sangto.rental_car_server.domain.dto.user.AddUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public RegisterUserResponseDTO toRegisterUserResponseDTO(User entity) {
        return RegisterUserResponseDTO.builder()
                .accessToken(jwtTokenUtil.generateToken(entity))
                .userDetail(toUserDetailResponseDTO(entity))
                .build();
    }

    @Override
    public UserDetailResponseDTO toUserDetailResponseDTO(User entity) {
        return UserDetailResponseDTO.builder()
                .userId(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .isActive(entity.isActive())
                .role(entity.getRole())
                .build();
    }

    @Override
    public UserResponseDTO toUserResponseDTO(User entity) {
        return UserResponseDTO.builder()
                .userId(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .isActive(entity.isActive())
                .role(entity.getRole())
                .build();
    }

    @Override
    public User addUserRequestDTOtoEntity(AddUserRequestDTO requestDTO) {
        return User.builder()
                .username(requestDTO.username())
                .email(requestDTO.email())
                .isActive(true)
                .role(EUserRole.valueOf(requestDTO.role()))
                .build();
    }
}
