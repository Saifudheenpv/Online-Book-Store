package com.bookstore.repository;

import com.bookstore.entity.User;
import com.bookstore.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUser(User user);
    Optional<Wishlist> findByUserAndBookId(User user, Long bookId);
    boolean existsByUserAndBookId(User user, Long bookId);
    
    @Modifying
    @Query("DELETE FROM Wishlist w WHERE w.user = :user AND w.book.id = :bookId")
    void deleteByUserAndBookId(@Param("user") User user, @Param("bookId") Long bookId);
    
    @Query("SELECT COUNT(w) FROM Wishlist w WHERE w.user = :user")
    Integer getWishlistCount(@Param("user") User user);
}
