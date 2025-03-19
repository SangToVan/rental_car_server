package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.auth.LoginResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UpdUserRequestDTO;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.domain.mapper.UserMapper;
import com.sangto.rental_car_server.domain.dto.user.AddUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.UserRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.UserService;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Response<UserDetailResponseDTO> getDetailUser(Integer id) {
        Optional<User> findUser = userRepo.findById(id);
        if (findUser.isEmpty()) throw new AppException("This user is not existed");
        return Response.successfulResponse(
                "Get detail user successfully", userMapper.toUserDetailResponseDTO(findUser.get()));
    }

    @Override
    public Response<LoginResponseDTO> addUser(AddUserRequestDTO requestDTO) throws IOException {
        Optional<User> findUser = userRepo.findByEmail(requestDTO.email());
        if (!findUser.isEmpty()) throw new AppException("Email already existed");

        User newUser = userMapper.addUserRequestDTOtoEntity(requestDTO);
        // Set password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        newUser.setPassword(passwordEncoder.encode(requestDTO.password()));

        try {
            User saveUser = userRepo.save(newUser);
            return Response.successfulResponse(
                    "Register successfully",
                    LoginResponseDTO.builder()
                            .authenticated(true)
                            .token(jwtTokenUtil.generateToken(saveUser))
                            .info(userMapper.toUserDetailResponseDTO(saveUser))
                            .build());
        } catch (Exception e) {
            throw new AppException("Register unsuccessfully");
        }
    }

    @Override
    public Response<UserDetailResponseDTO> updateUser(Integer id, UpdUserRequestDTO requestDTO) {
        Optional<User> oldUserOpt = userRepo.findById(id);
        if (oldUserOpt.isEmpty()) throw new AppException("This user is not existed");

        User oldUser = oldUserOpt.get();

        User newUser = userMapper.updUserRequestDTOtoEntity(oldUser, requestDTO);

        try {
            User saveUser = userRepo.save(newUser);
            return Response.successfulResponse("Update user successfully", userMapper.toUserDetailResponseDTO(saveUser));
        } catch (Exception e) {
            throw new AppException("Update user unsuccessfully");
        }
    }

    @Override
    public Response<UserDetailResponseDTO> changeUserRole(Integer id) {
        Optional<User> findUser = userRepo.findById(id);
        if (findUser.isEmpty()) throw new AppException("This user is not existed");
        User user = findUser.get();
        if (user.getRole() == EUserRole.CUSTOMER) {
            user.setRole(EUserRole.OWNER);
        } else {
            user.setRole(EUserRole.CUSTOMER);
        }

        try {
            User saveUser = userRepo.save(user);
            return Response.successfulResponse("Change user role successfully", userMapper.toUserDetailResponseDTO(saveUser));
        } catch (Exception e) {
            throw new AppException("Change user role unsuccessfully");
        }
    }
}
