package com.sangto.rental_car_server.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ERelationship {
    FAMILY("Người thân"),
    FRIEND("Bạn bè"),
    COLLEAGUE("Đồng nghiệp"),
    DRIVER("Tài xế thuê"),
    OTHER("Khác");

    private final String title;
}
