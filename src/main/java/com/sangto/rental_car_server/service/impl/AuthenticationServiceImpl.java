package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.auth.LoginRequestDTO;
import com.sangto.rental_car_server.domain.dto.auth.LoginResponseDTO;
import com.sangto.rental_car_server.domain.mapper.UserMapper;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.AuthenticationService;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepo;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;

    @Override
    public Response<LoginResponseDTO> login(LoginRequestDTO requestDTO) {
        Optional<User> userResult = userRepo.findByEmail(requestDTO.email());

        if (userResult.isEmpty())
            throw new AppException("Either email address or password is incorrect. Please try again");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Boolean authenticated =
                passwordEncoder.matches(requestDTO.password(), userResult.get().getPassword());
        if (authenticated == false)
            throw new AppException("Either email address or password is incorrect. Please try again");
        String token = jwtTokenUtil.generateToken(userResult.get());

        LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                .authenticated(true)
                .token(token)
                .info(userMapper.toUserDetailResponseDTO(userResult.get()))
                .build();
        return Response.successfulResponse("Login successfully", responseDTO);
    }
}
