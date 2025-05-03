package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.CarBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarBrandRepository extends JpaRepository<CarBrand, Integer> {

    Optional<CarBrand> findByName(String name);

    boolean existsByName(String name);
}
