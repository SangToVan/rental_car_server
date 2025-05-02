package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.auth.RegisterUserResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UpdUserRequestDTO;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.mapper.UserMapper;
import com.sangto.rental_car_server.domain.dto.user.AddUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

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
                .birthday(entity.getBirthday().toString())
                .citizenId(entity.getCitizenId())
                .phoneNumber(entity.getPhoneNumber())
                .address(entity.getAddress())
                .isActive(entity.isActive())
                .balance(entity.getWallet().getBalance().toString())
                .role(entity.getRole())
                .build();
    }

    @Override
    public UserResponseDTO toUserResponseDTO(User entity) {
        return UserResponseDTO.builder()
                .userId(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .balance(entity.getWallet().getBalance().toString())
                .isActive(entity.isActive())
                .role(entity.getRole())
                .build();
    }

    @Override
    public User addUserRequestDTOtoEntity(AddUserRequestDTO requestDTO) {
        return User.builder()
                .username(requestDTO.username())
                .email(requestDTO.email())
                .birthday(LocalDate.now())
                .citizenId(null)
                .phoneNumber(requestDTO.phoneNumber())
                .address(null)
                .isActive(true)
                .wallet(new Wallet())
                .role(EUserRole.CUSTOMER)
                .build();
    }

    @Override
    public User updUserRequestDTOtoEntity(User oldUser, UpdUserRequestDTO requestDTO) {
        oldUser.setUsername(requestDTO.username());
        oldUser.setBirthday(requestDTO.birthday());
        oldUser.setCitizenId(requestDTO.citizenId());
        oldUser.setPhoneNumber(requestDTO.phoneNumber());
        oldUser.setAddress(requestDTO.address());

        return oldUser;
    }
}
