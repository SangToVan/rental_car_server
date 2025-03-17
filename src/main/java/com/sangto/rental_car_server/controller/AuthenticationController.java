package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.auth.LoginRequestDTO;
import com.sangto.rental_car_server.domain.dto.auth.LoginResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.AddUserRequestDTO;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.AuthenticationService;
import com.sangto.rental_car_server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
