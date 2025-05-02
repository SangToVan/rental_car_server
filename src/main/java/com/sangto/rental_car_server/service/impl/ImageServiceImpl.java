package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.dto.image.UpdImageRequestDTO;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Override
    public Response<ImageResponseDTO> updImage(UpdImageRequestDTO requestDTO, String folder) {
        return null;
    }
}
