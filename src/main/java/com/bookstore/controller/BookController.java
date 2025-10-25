package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.entity.User;
import com.bookstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private WishlistService wishlistService;
    
    @Autowired
    private ReviewService reviewService;
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @GetMapping
    public String listBooks(Model model, 
                           @RequestParam(required = false) String search,
                           @RequestParam(required = false) String category,
                           @RequestParam(required = false) String sort) {
        List<Book> books;
        
        if (search != null && !search.trim().isEmpty()) {
            books = bookService.searchBooks(search);
            model.addAttribute("search", search);
        } else if (category != null && !category.trim().isEmpty()) {
            books = bookService.getBooksByCategory(category);
            model.addAttribute("category", category);
        } else {
            books = bookService.getAllBooks();
        }
        
        // Apply sorting
        if (sort != null) {
            switch (sort) {
                case "price-low":
                    books.sort((b1, b2) -> b1.getPrice().compareTo(b2.getPrice()));
                    break;
                case "price-high":
                    books.sort((b1, b2) -> b2.getPrice().compareTo(b1.getPrice()));
                    break;
                case "title":
                    books.sort((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
                    break;
                case "newest":
                    // Already sorted by createdAt desc by default
                    break;
            }
        }
        
        User user = getCurrentUser();
        int cartItemCount = cartService.getCartItemsCount(user);
        int wishlistCount = wishlistService.getWishlistCount(user);
        
        model.addAttribute("books", books);
        model.addAttribute("cartItemCount", cartItemCount);
        model.addAttribute("wishlistCount", wishlistCount);
        model.addAttribute("categories", List.of("Fantasy", "Science Fiction", "Romance", "Mystery", "History", "Education"));
        
        return "books";
    }
    
    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isEmpty()) {
            return "redirect:/books";
        }
        
        User user = getCurrentUser();
        int cartItemCount = cartService.getCartItemsCount(user);
        int wishlistCount = wishlistService.getWishlistCount(user);
        boolean inWishlist = wishlistService.isBookInWishlist(user, id);
        var reviews = reviewService.getReviewsByBook(book.get());
        double averageRating = reviewService.getAverageRating(book.get());
        int reviewCount = reviewService.getReviewCount(book.get());
        var userReview = reviewService.getUserReviewForBook(user, book.get());
        
        model.addAttribute("book", book.get());
        model.addAttribute("cartItemCount", cartItemCount);
        model.addAttribute("wishlistCount", wishlistCount);
        model.addAttribute("inWishlist", inWishlist);
        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("reviewCount", reviewCount);
        model.addAttribute("userReview", userReview.orElse(null));
        
        return "book-detail";
    }
}
