package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {

}
