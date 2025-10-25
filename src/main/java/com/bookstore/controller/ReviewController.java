package com.bookstore.controller;

import com.bookstore.entity.User;
import com.bookstore.service.ReviewService;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private UserService userService;
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @PostMapping("/add")
    public String addReview(@RequestParam Long bookId,
                           @RequestParam Integer rating,
                           @RequestParam String comment) {
        User user = getCurrentUser();
        reviewService.addReview(user, bookId, rating, comment);
        return "redirect:/books/" + bookId + "?reviewSuccess";
    }
}
