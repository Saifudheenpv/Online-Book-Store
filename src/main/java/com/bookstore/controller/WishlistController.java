package com.bookstore.controller;

import com.bookstore.entity.User;
import com.bookstore.service.UserService;
import com.bookstore.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {
    
    @Autowired
    private WishlistService wishlistService;
    
    @Autowired
    private UserService userService;
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @GetMapping
    public String viewWishlist(Model model) {
        User user = getCurrentUser();
        var wishlistItems = wishlistService.getWishlist(user);
        int wishlistCount = wishlistService.getWishlistCount(user);
        
        model.addAttribute("wishlistItems", wishlistItems);
        model.addAttribute("wishlistCount", wishlistCount);
        return "wishlist";
    }
    
    @PostMapping("/add")
    public String addToWishlist(@RequestParam Long bookId) {
        User user = getCurrentUser();
        wishlistService.addToWishlist(user, bookId);
        return "redirect:/books";
    }
    
    @PostMapping("/remove")
    public String removeFromWishlist(@RequestParam Long bookId) {
        User user = getCurrentUser();
        wishlistService.removeFromWishlist(user, bookId);
        return "redirect:/wishlist";
    }
}
