package com.sangto.rental_car_server.service.impl;

import com.sangto.rental_car_server.domain.dto.image.ImageResponseDTO;
import com.sangto.rental_car_server.domain.dto.image.UpdImageRequestDTO;
import com.sangto.rental_car_server.domain.entity.Image;
import com.sangto.rental_car_server.domain.mapper.ImageMapper;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.repository.ImageRepository;
import com.sangto.rental_car_server.responses.Response;
import com.sangto.rental_car_server.service.CloudinaryService;
import com.sangto.rental_car_server.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageMapper imageMapper;
    private final ImageRepository imageRepo;
    private final CloudinaryService cloudinaryService;

    @Override
    public Response<ImageResponseDTO> updImage(UpdImageRequestDTO requestDTO, String folder) {
        Optional<Image> findImage = imageRepo.findById(requestDTO.id());
        if (findImage.isEmpty()) throw new AppException("Image is not existed");
        Image updImage = findImage.get();
        // Delete Image on Cloudinary
        try {
            cloudinaryService.deleteFile(findImage.get().getImagePublicId());
        } catch (IOException e) {
            throw new AppException("Delete image on cloudinary unsuccessfully");
        }
        // Upload New Image to Cloudinary
        try {
            Map uploadImage = cloudinaryService.uploadFileBase64(requestDTO.imageItem(), folder);
            updImage.setName((String) uploadImage.get("original_filename"));
            updImage.setImageUrl((String) uploadImage.get("url"));
            updImage.setImagePublicId((String) uploadImage.get("public_id"));
            Image saveImage = imageRepo.save(updImage);
            return Response.successfulResponse("Update image successful", imageMapper.toImageResponseDTO(saveImage));
        } catch (IOException e) {
            throw new AppException("Upload image unsuccessfully");
        }
    }
}
