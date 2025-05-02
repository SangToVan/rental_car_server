package com.sangto.rental_car_server.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CloudinaryService {
    Map uploadFileBase64(String base64String, String folder) throws IOException;

    Map uploadFile(MultipartFile multipartFile, String folder) throws IOException;

    List<Map> uploadFiles(List<MultipartFile> files, String folder) throws IOException;

    Map deleteFile(String publicId) throws IOException;

    Map deleteFiles(List<Integer> ids);
}
