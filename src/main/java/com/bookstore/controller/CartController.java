package com.bookstore.controller;

import com.bookstore.entity.User;
import com.bookstore.service.CartService;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserService userService;
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @GetMapping
    public String viewCart(Model model) {
        User user = getCurrentUser();
        var cartItems = cartService.getCartItems(user);
        double total = cartService.getCartTotal(user);
        int cartItemCount = cartService.getCartItemsCount(user);
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("cartItemCount", cartItemCount);
        return "cart";
    }
    
    @PostMapping("/add")
    public String addToCart(@RequestParam Long bookId, 
                           @RequestParam(defaultValue = "1") Integer quantity) {
        User user = getCurrentUser();
        cartService.addToCart(user, bookId, quantity);
        return "redirect:/books";
    }
    
    @PostMapping("/update")
    public String updateCartItem(@RequestParam Long bookId, 
                                @RequestParam Integer quantity) {
        User user = getCurrentUser();
        cartService.updateCartItemQuantity(user, bookId, quantity);
        return "redirect:/cart";
    }
    
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long bookId) {
        User user = getCurrentUser();
        cartService.removeFromCart(user, bookId);
        return "redirect:/cart";
    }
    
    @PostMapping("/clear")
    public String clearCart() {
        User user = getCurrentUser();
        cartService.clearCart(user);
        return "redirect:/cart";
    }
}
