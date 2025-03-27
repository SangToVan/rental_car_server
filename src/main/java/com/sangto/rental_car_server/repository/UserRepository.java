package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.User;
import com.sangto.rental_car_server.domain.entity.Wallet;
import com.sangto.rental_car_server.domain.enums.EUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u.wallet FROM User u WHERE u.id = :userId")
    Optional<Wallet> findWalletById(@Param("userId") Integer userId);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") EUserRole role);
}
