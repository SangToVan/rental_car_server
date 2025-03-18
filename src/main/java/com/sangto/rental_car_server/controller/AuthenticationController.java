package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.auth.ChangePasswordRequestDTO;
import com.sangto.rental_car_server.domain.dto.auth.LoginRequestDTO;
import com.sangto.rental_car_server.domain.dto.auth.LoginResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.AddUserRequestDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.AuthenticationService;
import com.sangto.rental_car_server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Operation(summary = "Log in", description = "This API allows users to log in.")
    @PostMapping(Endpoint.V1.Auth.LOGIN)
    public ResponseEntity<Response<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.login(requestDTO));
    }

    @Operation(summary = "Register an account", description = "This API allows users to register an account")
    @PostMapping(Endpoint.V1.Auth.REGISTER)
    public ResponseEntity<Response<LoginResponseDTO>> register(
            // @ModelAttribute
            @RequestBody @Valid AddUserRequestDTO requestDTO) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.addUser(requestDTO));
    }

    @Operation(summary = "Change password", description = "This API allows users to change password")
    @PatchMapping(Endpoint.V1.Auth.CHANGE_PASSWORD)
    public ResponseEntity<Response<String>> changePassword(@RequestBody @Valid ChangePasswordRequestDTO requestDTO) {
        User user =
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK)
                .body(authenticationService.changePassword(user.getId(), requestDTO));
    }
}
