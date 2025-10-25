package com.bookstore.repository;

import com.bookstore.entity.Order;
import com.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findByStatus(String status);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.user = :user")
    Integer getOrderCountByUser(@Param("user") User user);
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.user = :user AND o.paymentStatus = 'COMPLETED'")
    Double getTotalSpentByUser(@Param("user") User user);
}
