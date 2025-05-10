package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.feedback.FeedbackResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.responses.MetaResponse;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.FeedbackService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "Feedbacks")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping(Endpoint.V1.Feedback.LIST_FOR_CAR)
    public ResponseEntity<MetaResponse<MetaResponseDTO, List<FeedbackResponseDTO>>> getListFeedbackForCar(
            @PathVariable(name = "paymentId") Integer carId,
            @ParameterObject MetaRequestDTO metaRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(feedbackService.getListFeedbackByCar(carId, metaRequestDTO));
    }

    @GetMapping(Endpoint.V1.Feedback.GET_RATING)
    public ResponseEntity<Response<Map<String, String>>> getRating(@PathVariable(name = "paymentId") Integer carId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(feedbackService.getRating(carId));
    }
}