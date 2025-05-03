package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.CarBrand;
import com.sangto.rental_car_server.domain.entity.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Integer> {

    List<CarModel> findByBrand(CarBrand brand);

    boolean existsByNameAndBrand(String name, CarBrand brand);

    Optional<CarModel> findByName(String name);
}
