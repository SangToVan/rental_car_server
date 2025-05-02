package com.sangto.rental_car_server.domain.mapper;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.entity.Image;

public interface ImageMapper {
    ImageResponseDTO toImageResponseDTO(Image entity);
}
