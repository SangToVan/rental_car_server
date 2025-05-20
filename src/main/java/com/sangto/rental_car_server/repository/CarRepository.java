package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Car;
import com.sangto.rental_car_server.domain.entity.CarModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    @Query("SELECT DISTINCT c FROM Car c " + " JOIN FETCH c.carOwner co "
            + "LEFT JOIN FETCH c.images i "
            + " WHERE co.id = :ownerId ")
    Page<Car> getListCarByOwner(@Param("ownerId") Integer ownerId, Pageable pageable);

//    @Query("""
//    SELECT DISTINCT c FROM Car c
//    JOIN FETCH c.carOwner co
//    LEFT JOIN FETCH c.model m
//    LEFT JOIN FETCH m.brand b
//    LEFT JOIN FETCH c.images i
//    WHERE co.paymentId = :ownerId
//    AND CONCAT(c.name, ' ', m.name, ' ', b.name, ' ' , c.address) LIKE %:keyword%
//""")
//    Page<Car> getListCarByOwnerWithKeyword(
//            @Param("ownerId") Integer ownerId,
//            @Param("keyword") String keyword,
//            Pageable pageable);

    @Query("""
    SELECT DISTINCT c FROM Car c
    JOIN FETCH c.carOwner co
    LEFT JOIN FETCH c.images i
    WHERE co.id = :ownerId
    AND CONCAT(c.name, ' ', c.brand, ' ', c.model, ' ' , c.address) LIKE %:keyword%
""")
    Page<Car> getListCarByOwnerWithKeyword(
            @Param("ownerId") Integer ownerId,
            @Param("keyword") String keyword,
            Pageable pageable);


    @Query("SELECT DISTINCT c FROM Car c " + " JOIN FETCH c.carOwner co ")
    List<Car> getAllCars();

    @Query("SELECT c FROM Car c " + "JOIN FETCH c.carOwner "
            +
            //            "JOIN FETCH c.imageList " +
            "WHERE c.id = :id")
    Optional<Car> findByIdWithOwner(@Param("id") Integer id);

    @Query(value = """
    SELECT * FROM cars c
    WHERE c.status != 'UNVERIFIED'
      AND c.status != 'SUSPENDED'
      AND (:address IS NULL OR LOWER(c.address) LIKE LOWER(:address))
      AND (:brand IS NULL OR c.brand = :brand)
      AND (:numberOfSeats IS NULL OR c.number_of_seats = :numberOfSeats)
      AND (:transmission IS NULL OR c.transmission = :transmission)
      AND (:fuelType IS NULL OR c.fuel_type = :fuelType)
      AND (:minPrice IS NULL OR c.base_price >= :minPrice)
      AND (:maxPrice IS NULL OR c.base_price <= :maxPrice)
      AND c.car_id NOT IN (
        SELECT b.car_id FROM bookings b
        WHERE b.status != 'COMPLETED' AND b.status != 'CANCELLED'
          AND (
            (b.end_date_time >= :endTime AND b.start_date_time <= :endTime)
            OR (b.start_date_time <= :startTime AND b.end_date_time >= :startTime)
            OR (b.end_date_time <= :endTime AND b.start_date_time >= :startTime)
          )
      )
""", nativeQuery = true)
    Page<Car> searchCarV2(
            @Param("address") String address,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("brand") String brand,
            @Param("numberOfSeats") Integer numberOfSeats,
            @Param("transmission") String transmission,
            @Param("fuelType") String fuelType,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );

    @Query(
            value =
                    "SELECT * FROM cars c\n"
                            + "WHERE c.car_id = :carId AND c.`status` != 'UNVERIFIED' AND c.`status` != 'SUSPENDED' AND c.car_id NOT IN (\n"
                            + "\t\t\t\t\tSELECT b.car_id FROM bookings b \n"
                            + "                    WHERE b.`status` != 'COMPLETED' AND b.`status` != 'CANCELLED' \n"
                            + "\t\t\t\t\t\tAND ((b.end_date_time >= :endTime AND b.start_date_time <= :endTime) \n"
                            + "\t\t\t\t\t\t\t\tOR (b.start_date_time <= :startTime AND b.end_date_time >= :startTime) \n"
                            + "                                OR (b.end_date_time <= :endTime AND b.start_date_time >= :startTime)))",
            nativeQuery = true)
    Optional<Car> checkScheduleCar(
            @Param("carId") Integer carId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    Optional<Car> findCarById(Integer id);

    List<Car> findAllByCarOwnerId(Integer ownerId);

//    int countByModel(CarModel model);
//
//    @Query("SELECT c.model.brand, COUNT(c) FROM Car c GROUP BY c.model.brand")
//    List<Object[]> countCarsByBrand();
//
//    @Query("SELECT c.model, COUNT(c) FROM Car c GROUP BY c.model")
//    List<Object[]> countCarsByModel();

    @Query(value = "SELECT * FROM cars ORDER BY rating DESC LIMIT 8", nativeQuery = true)
    List<Car> findTop8CarsByRating();

    @Query("SELECT c FROM Car c WHERE c.status = 'UNVERIFIED' ORDER BY c.createdAt DESC")
    List<Car> findAllUnverifiedCars();

    @Query("SELECT c FROM Car c WHERE c.status = 'WAITING' ORDER BY c.createdAt DESC")
    List<Car> findAllWaitingCars();

    @Query("SELECT c FROM Car c WHERE c.status != 'UNVERIFIED'AND c.status != 'WAITING' ORDER BY c.createdAt DESC")
    List<Car> findAllOtherStatusCars();

}
