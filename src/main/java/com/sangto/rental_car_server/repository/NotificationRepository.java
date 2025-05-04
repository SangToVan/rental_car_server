package com.sangto.rental_car_server.repository;

import com.sangto.rental_car_server.domain.entity.Notification;
import com.sangto.rental_car_server.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    // Lấy tất cả thông báo của user, mới nhất trước
    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    // Lấy thông báo chưa đọc
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.isRead = false ORDER BY n.createdAt DESC")
    List<Notification> findByUserAndIsReadFalseOrderByCreatedAtDesc(@Param("userId") Integer userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.isRead = false")
    long countUnreadByUserId(@Param("userId") Integer userId);

    // Xoá tất cả thông báo của user (nếu không dùng ON DELETE CASCADE)
    void deleteByUser(User user);

    // (Optional) Lấy thông báo theo trang (nếu dùng @Query với phân trang)
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.createdAt DESC")
    List<Notification> findAllByUserId(@Param("userId") Integer userId);
}
