package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private BookService bookService;
    
    public Order createOrder(User user, String shippingAddress, String paymentMethod) {
        // Get cart items
        List<CartItem> cartItems = cartService.getCartItems(user);
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        // Calculate total amount
        BigDecimal totalAmount = BigDecimal.valueOf(cartService.getCartTotal(user));
        
        // Generate order number
        String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // Create order
        Order order = new Order(user, orderNumber, totalAmount);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setStatus("CONFIRMED");
        order.setPaymentStatus("COMPLETED");
        
        Order savedOrder = orderRepository.save(order);
        
        // Create order items and update book stock
        for (CartItem cartItem : cartItems) {
            Book book = cartItem.getBook();
            OrderItem orderItem = new OrderItem(savedOrder, book, cartItem.getQuantity(), book.getPrice());
            orderItemRepository.save(orderItem);
            
            // Update book stock
            book.setStockQuantity(book.getStockQuantity() - cartItem.getQuantity());
            bookService.saveBook(book);
        }
        
        // Clear cart after order
        cartService.clearCart(user);
        
        return savedOrder;
    }
    
    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
    
    public Optional<Order> getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
    
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
    
    public Integer getOrderCount(User user) {
        Integer count = orderRepository.getOrderCountByUser(user);
        return count != null ? count : 0;
    }
    
    public Double getTotalSpent(User user) {
        Double total = orderRepository.getTotalSpentByUser(user);
        return total != null ? total : 0.0;
    }
}
