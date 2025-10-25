package com.bookstore.service;

import com.bookstore.entity.User;
import com.bookstore.entity.Wishlist;
import com.bookstore.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class WishlistService {
    
    @Autowired
    private WishlistRepository wishlistRepository;
    
    @Autowired
    private BookService bookService;
    
    public void addToWishlist(User user, Long bookId) {
        if (!wishlistRepository.existsByUserAndBookId(user, bookId)) {
            var book = bookService.getBookById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
            
            Wishlist wishlist = new Wishlist(user, book);
            wishlistRepository.save(wishlist);
        }
    }
    
    public void removeFromWishlist(User user, Long bookId) {
        wishlistRepository.deleteByUserAndBookId(user, bookId);
    }
    
    public List<Wishlist> getWishlist(User user) {
        return wishlistRepository.findByUser(user);
    }
    
    public boolean isBookInWishlist(User user, Long bookId) {
        return wishlistRepository.existsByUserAndBookId(user, bookId);
    }
    
    public Integer getWishlistCount(User user) {
        Integer count = wishlistRepository.getWishlistCount(user);
        return count != null ? count : 0;
    }
}
