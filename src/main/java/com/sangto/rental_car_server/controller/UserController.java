package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.user.UpdUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.wallet.UpdWalletDTO;
import com.sangto.rental_car_server.domain.dto.wallet.WalletResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.UserService;
import com.sangto.rental_car_server.service.WalletService;
import com.sangto.rental_car_server.utility.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Operation(summary = "View profile")
    @GetMapping(Endpoint.V1.User.PROFILE)
    public ResponseEntity<Response<UserDetailResponseDTO>> getDetailUser() {
        User user =
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(userService.getDetailUser(user.getId()));
    }

    @Operation(summary = "Update profile")
    @PatchMapping(Endpoint.V1.User.PROFILE)
    public ResponseEntity<Response<UserDetailResponseDTO>> updateUser(
            @RequestBody @Valid UpdUserRequestDTO requestDTO) {
        User user =
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(user.getId(), requestDTO));
    }

    @Operation(summary = "Change role")
    @GetMapping(Endpoint.V1.User.CHANGE_ROLE)
    public ResponseEntity<Response<UserDetailResponseDTO>> changeRole() {
        User user =
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(userService.changeUserRole(user.getId()));
    }

    @Operation(summary = "Get wallet detail")
    @GetMapping(Endpoint.V1.User.GET_WALLET)
    public ResponseEntity<Response<WalletResponseDTO>> getMyWallet() {
        Integer userId = AuthUtil.getRequestedUser().getId();
        return ResponseEntity.status(HttpStatus.OK).body(
                walletService.getWalletDetail(userId)
        );
    }

    @Operation(summary = "Top up / Withdraw wallet")
    @PatchMapping(Endpoint.V1.User.UPDATE_WALLET)
    public ResponseEntity<Response<WalletResponseDTO>> updateWallet(@RequestBody UpdWalletDTO requestDTO) {
        Integer userId = AuthUtil.getRequestedUser().getId();
        return ResponseEntity.status(HttpStatus.OK).body(
                walletService.updateWallet(userId, requestDTO)
        );
    }
}
