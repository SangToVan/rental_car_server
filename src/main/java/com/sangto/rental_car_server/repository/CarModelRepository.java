package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Integer> {
}
