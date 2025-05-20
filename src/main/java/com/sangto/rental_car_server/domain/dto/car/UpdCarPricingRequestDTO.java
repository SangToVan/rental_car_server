package com.sangto.rental_car_server.domain.dto.car;

public record UpdCarPricingRequestDTO(
        // Price information (Can change)
        String basePrice,
        Boolean quickRent,
        Integer maxDeliveryDistance,
        Integer deliveryFee,
        Integer freeDeliveryDistance,
        Integer kmPerDay,
        Integer kmOverDayFee,
        Integer discountPerWeek
) {
}
