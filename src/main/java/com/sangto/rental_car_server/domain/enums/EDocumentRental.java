package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EDocumentRental {
    NATIVE("Công dân Việt Nam"),
    FOREIGNER("Người nước ngoài"),
    NONE("Không có bằng lái");


    private final String title;
}
