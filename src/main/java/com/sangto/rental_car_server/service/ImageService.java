package com.sangto.rental_car_server.service;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.dto.image.UpdImageRequestDTO;
import com.sangto.rental_car_server.responses.Response;

public interface ImageService {
    Response<ImageResponseDTO> updImage(UpdImageRequestDTO requestDTO, String folder);
}
