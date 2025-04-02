package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.escrow_transaction.EscrowTransactionResponseDTO;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.EscrowTransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Escrow_transactions")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class EscrowTransactionController {
    private final EscrowTransactionService escrowTransactionService;

    @GetMapping(Endpoint.V1.EscrowTransaction.BASE)
    public ResponseEntity<Response<List<EscrowTransactionResponseDTO>>> getListEscrowTransactions() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(escrowTransactionService.getAllEscrowTransactions());
    }
}
