package com.bookstore.controller;

import com.bookstore.entity.User;
import com.bookstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private WishlistService wishlistService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ReviewService reviewService;
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @GetMapping("/dashboard")
    public String userDashboard(Model model) {
        User user = getCurrentUser();
        int cartItemCount = cartService.getCartItemsCount(user);
        int wishlistCount = wishlistService.getWishlistCount(user);
        int orderCount = orderService.getOrderCount(user);
        double totalSpent = orderService.getTotalSpent(user);
        var recentOrders = orderService.getUserOrders(user);
        if (recentOrders.size() > 3) {
            recentOrders = recentOrders.subList(0, 3);
        }
        
        model.addAttribute("currentUser", user);
        model.addAttribute("cartItemCount", cartItemCount);
        model.addAttribute("wishlistCount", wishlistCount);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("pageTitle", "Dashboard");
        
        return "user-dashboard";
    }
    
    @GetMapping("/profile")
    public String userProfile(Model model) {
        User user = getCurrentUser();
        int cartItemCount = cartService.getCartItemsCount(user);
        int wishlistCount = wishlistService.getWishlistCount(user);
        int orderCount = orderService.getOrderCount(user);
        double totalSpent = orderService.getTotalSpent(user);
        var userReviews = reviewService.getReviewsByUser(user);
        
        model.addAttribute("currentUser", user);
        model.addAttribute("cartItemCount", cartItemCount);
        model.addAttribute("wishlistCount", wishlistCount);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("userReviews", userReviews);
        model.addAttribute("pageTitle", "My Profile");
        
        return "user-profile";
    }
}
