package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Review;
import com.bookstore.entity.User;
import com.bookstore.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private BookService bookService;
    
    public Review addReview(User user, Long bookId, Integer rating, String comment) {
        var book = bookService.getBookById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        
        // Check if user already reviewed this book
        Optional<Review> existingReview = reviewRepository.findByUserAndBook(user, book);
        
        if (existingReview.isPresent()) {
            Review review = existingReview.get();
            review.setRating(rating);
            review.setComment(comment);
            return reviewRepository.save(review);
        } else {
            Review review = new Review(user, book, rating, comment);
            return reviewRepository.save(review);
        }
    }
    
    public List<Review> getReviewsByBook(Book book) {
        return reviewRepository.findByBookOrderByCreatedAtDesc(book);
    }
    
    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findByUser(user);
    }
    
    public Double getAverageRating(Book book) {
        Double avg = reviewRepository.getAverageRatingByBook(book);
        return avg != null ? avg : 0.0;
    }
    
    public Integer getReviewCount(Book book) {
        Integer count = reviewRepository.getReviewCountByBook(book);
        return count != null ? count : 0;
    }
    
    public Optional<Review> getUserReviewForBook(User user, Book book) {
        return reviewRepository.findByUserAndBook(user, book);
    }
}
