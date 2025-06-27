package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.feedback.AddFeedbackRequestDTO;
import com.sangto.rental_car_server.domain.dto.feedback.FeedbackResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.domain.entity.Feedback;
import com.sangto.rental_car_server.responses.MetaResponse;
import com.sangto.rental_car_server.responses.Response;

import java.util.List;
import java.util.Map;

public interface FeedbackService {
    Response<String> addFeedback(Integer userId, Integer bookingId, AddFeedbackRequestDTO requestDTO);

    MetaResponse<MetaResponseDTO, List<FeedbackResponseDTO>> getListFeedbackByOwner(
            Integer ownerId, Integer rating, MetaRequestDTO metaRequestDTO);

    MetaResponse<MetaResponseDTO, List<FeedbackResponseDTO>> getListFeedbackByCar(
            Integer carId, MetaRequestDTO metaRequestDTO);

    Response<Map<String, String>> getRating(Integer carId);
}
