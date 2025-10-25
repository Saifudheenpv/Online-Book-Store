package com.bookstore.controller;

import com.bookstore.entity.User;
import com.bookstore.service.OrderService;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @GetMapping
    public String viewOrders(Model model) {
        User user = getCurrentUser();
        var orders = orderService.getUserOrders(user);
        int orderCount = orderService.getOrderCount(user);
        double totalSpent = orderService.getTotalSpent(user);
        
        model.addAttribute("orders", orders);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("totalSpent", totalSpent);
        return "orders";
    }
    
    @GetMapping("/{orderNumber}")
    public String viewOrderDetails(@PathVariable String orderNumber, Model model) {
        var order = orderService.getOrderByNumber(orderNumber)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        
        model.addAttribute("order", order);
        return "order-details";
    }
    
    @PostMapping("/create")
    public String createOrder(@RequestParam String shippingAddress, 
                             @RequestParam String paymentMethod) {
        User user = getCurrentUser();
        var order = orderService.createOrder(user, shippingAddress, paymentMethod);
        return "redirect:/orders/" + order.getOrderNumber() + "?success";
    }
}
