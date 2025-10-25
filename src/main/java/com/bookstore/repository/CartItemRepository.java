package com.bookstore.repository;

import com.bookstore.entity.CartItem;
import com.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndBookId(User user, Long bookId);
    
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.user = :user")
    void deleteByUser(@Param("user") User user);
    
    @Query("SELECT SUM(c.quantity) FROM CartItem c WHERE c.user = :user")
    Integer getTotalCartItemsCount(@Param("user") User user);
}
