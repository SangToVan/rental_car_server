package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    @Query("SELECT DISTINCT c FROM Car c " + " JOIN FETCH c.carOwner co "
            + " WHERE co.id = :ownerId ")
    List<Car> getListCarByOwner(@Param("ownerId") Integer ownerId);

    @Query("SELECT DISTINCT c FROM Car c " + " JOIN FETCH c.carOwner co ")
    List<Car> getAllCars();

    Optional<Car> findCarById(Integer id);

    List<Car> findAllByCarOwnerId(Integer ownerId);
}
