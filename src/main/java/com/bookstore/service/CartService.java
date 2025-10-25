package com.bookstore.service;

import com.bookstore.entity.CartItem;
import com.bookstore.entity.User;
import com.bookstore.entity.Book;
import com.bookstore.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private BookService bookService;
    
    public void addToCart(User user, Long bookId, Integer quantity) {
        Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndBookId(user, bookId);
        
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            Book book = bookService.getBookById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
            
            CartItem newCartItem = new CartItem(user, book, quantity);
            cartItemRepository.save(newCartItem);
        }
    }
    
    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }
    
    public void updateCartItemQuantity(User user, Long bookId, Integer quantity) {
        if (quantity <= 0) {
            removeFromCart(user, bookId);
            return;
        }
        
        CartItem cartItem = cartItemRepository.findByUserAndBookId(user, bookId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }
    
    public void removeFromCart(User user, Long bookId) {
        CartItem cartItem = cartItemRepository.findByUserAndBookId(user, bookId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        cartItemRepository.delete(cartItem);
    }
    
    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }
    
    public Integer getCartItemsCount(User user) {
        Integer count = cartItemRepository.getTotalCartItemsCount(user);
        return count != null ? count : 0;
    }
    
    public Double getCartTotal(User user) {
        List<CartItem> cartItems = getCartItems(user);
        return cartItems.stream()
            .mapToDouble(item -> item.getBook().getPrice().doubleValue() * item.getQuantity())
            .sum();
    }
}
