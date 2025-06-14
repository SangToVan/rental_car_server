package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.user.UpdUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.wallet.UpdWalletRequestDTO;
import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.UserService;
import com.sangto.rental_car_server.service.WalletService;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final WalletService walletService;
    private final JwtTokenUtil jwtTokenUtil;

    @Operation(summary = "View profile")
    @GetMapping(Endpoint.V1.User.PROFILE)
    public ResponseEntity<Response<UserDetailResponseDTO>> getDetailUser(HttpServletRequest servletRequest) {
        Integer userId =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(userService.getDetailUser(userId));
    }

    @Operation(summary = "Update profile")
    @PatchMapping(Endpoint.V1.User.PROFILE)
    public ResponseEntity<Response<UserDetailResponseDTO>> updateUser(HttpServletRequest servletRequest,
            @RequestBody @Valid UpdUserRequestDTO requestDTO) {
        Integer userId =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userId, requestDTO));
    }

    @Operation(summary = "Change role")
    @GetMapping(Endpoint.V1.User.CHANGE_ROLE)
    public ResponseEntity<Response<UserDetailResponseDTO>> changeRole(HttpServletRequest servletRequest) {
        Integer userId =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(userService.changeUserRole(userId));
    }

    @Operation(summary = "Get wallet detail")
    @GetMapping(Endpoint.V1.User.GET_WALLET)
    public ResponseEntity<Response<WalletResponseDTO>> getMyWallet(HttpServletRequest servletRequest) {
        Integer userId =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(
                walletService.getWalletDetail(userId)
        );
    }

    @Operation(summary = "Top up / Withdraw wallet")
    @PatchMapping(Endpoint.V1.User.UPDATE_WALLET)
    public ResponseEntity<Response<String>> updateWallet(HttpServletRequest servletRequest, @RequestBody UpdWalletRequestDTO requestDTO) {
        Integer userId =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(
                walletService.updateWallet(userId, requestDTO)
        );
    }
}
