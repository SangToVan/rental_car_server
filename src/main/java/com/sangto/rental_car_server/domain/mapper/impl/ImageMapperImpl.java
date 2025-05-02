package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.entity.Image;
import com.sangto.rental_car_server.domain.mapper.ImageMapper;
import org.springframework.stereotype.Component;

@Component
public class ImageMapperImpl implements ImageMapper {
    @Override
    public ImageResponseDTO toImageResponseDTO(Image entity) {
        return ImageResponseDTO.builder()
                .id(entity.getId())
                .imageName(entity.getName())
                .imageUrl(entity.getImageUrl())
                .build();
    }
}
