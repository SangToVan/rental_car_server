package com.sangto.rental_car_server.domain.mapper.impl;

import com.sangto.rental_car_server.domain.dto.auth.RegisterUserResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UpdUserRequestDTO;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.enums.EVerifiedLicense;
import com.sangto.rental_car_server.domain.mapper.UserMapper;
import com.sangto.rental_car_server.domain.dto.user.AddUserRequestDTO;
import com.sangto.rental_car_server.domain.dto.user.UserDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.user.UserResponseDTO;
import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import com.sangto.rental_car_server.exceptions.AppException;
import com.sangto.rental_car_server.service.CloudinaryService;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final JwtTokenUtil jwtTokenUtil;
    private final CloudinaryService cloudinaryService;

    @Override
    public RegisterUserResponseDTO toRegisterUserResponseDTO(User entity) {
        return RegisterUserResponseDTO.builder()
                .accessToken(jwtTokenUtil.generateToken(entity))
                .userDetail(toUserDetailResponseDTO(entity))
                .build();
    }

    @Override
    public UserDetailResponseDTO toUserDetailResponseDTO(User entity) {
        return UserDetailResponseDTO.builder()
                .userId(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .role(entity.getRole())
                .birthday(entity.getBirthday())
                .gender(entity.getGender())
                .citizenId(entity.getCitizenId())
                .phoneNumber(entity.getPhoneNumber())
                .avatar(entity.getAvatar())
                .verifiedLicense(entity.getVerifiedLicense())
                .drivingLicense(entity.getDrivingLicense())
                .licenseFullName(entity.getLicenseFullName())
                .licenseBirthday(entity.getLicenseBirthday())
                .licenseImage(entity.getLicenseImage())
                .createdAt(entity.getCreatedAt())
                .isActive(entity.isActive())
                .balance(entity.getWallet().getBalance().toString())
                .build();
    }

    @Override
    public UserResponseDTO toUserResponseDTO(User entity) {
        return UserResponseDTO.builder()
                .userId(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .role(entity.getRole())
                .phoneNumber(entity.getPhoneNumber())
                .avatar(entity.getAvatar())
                .isActive(entity.isActive())
                .balance(entity.getWallet().getBalance().toString())
                .build();
    }

    @Override
    public User addUserRequestDTOtoEntity(AddUserRequestDTO requestDTO) {
        return User.builder()
                .username(requestDTO.username())
                .email(requestDTO.email())
                .role(EUserRole.CUSTOMER)
                .citizenId(null)
                .phoneNumber(requestDTO.phoneNumber())
                .address(null)
                .drivingLicense(null)
                .isActive(true)
                .wallet(new Wallet())
                .build();
    }

    @Override
    public User updUserRequestDTOtoEntity(User oldUser, UpdUserRequestDTO requestDTO) {
        oldUser.setUsername(requestDTO.username());
        oldUser.setBirthday(requestDTO.birthday());
        oldUser.setGender(requestDTO.gender());
        oldUser.setCitizenId(requestDTO.citizenId());
        oldUser.setPhoneNumber(requestDTO.phoneNumber());
        oldUser.setEmail(requestDTO.email());

        if (requestDTO.avatar() != null && !requestDTO.avatar().isEmpty()) {
            try {
                Map uploadResult = cloudinaryService.uploadFileBase64(requestDTO.avatar(), "avatar");
                String avatarUrl = (String) uploadResult.get("url");
                String avatarPublicId = (String) uploadResult.get("public_id");
                oldUser.setAvatar(avatarUrl);
                oldUser.setAvatarPublicId(avatarPublicId);
            } catch (IOException e) {
                throw new AppException("USER::UPLOAD_FILE_FAILED", e);
            }
        }

        oldUser.setVerifiedLicense(EVerifiedLicense.WAITING);
        oldUser.setDrivingLicense(requestDTO.drivingLicense());
        oldUser.setLicenseFullName(requestDTO.licenseFullName());
        oldUser.setLicenseBirthday(requestDTO.licenseBirthday());
        if(requestDTO.licenseImage() != null && !requestDTO.licenseImage().isEmpty()) {
            try {
                Map uploadResult = cloudinaryService.uploadFileBase64(requestDTO.licenseImage(), "license");
                String imageUrl = (String) uploadResult.get("url");
                String imagePublicId = (String) uploadResult.get("public_id");
                oldUser.setLicenseImage(imageUrl);
                oldUser.setLicenseImagePublicId(imagePublicId);
            } catch (IOException e) {
                throw new AppException("USER::UPLOAD_FILE_FAILED", e);
            }
        }

        return oldUser;
    }
}
