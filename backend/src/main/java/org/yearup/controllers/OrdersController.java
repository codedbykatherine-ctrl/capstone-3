package org.yearup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.models.Order;
import org.yearup.models.User;
import org.yearup.service.OrderService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
@PreAuthorize("hasRole('ROLE_USER')")
@CrossOrigin
public class OrdersController {
    //Service responsible for creating orders and handling checkout logic
    private OrderService orderService;
    //Service used to retrieve information about the authenticated user
    private UserService userService;

    //Constructor injection provides teh services needed by this controller
    public OrdersController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }
    //Handles the username of the currently authenticated user
    @PostMapping
    public ResponseEntity<Order> checkout(Principal principal) {

        // Retrieve the username of the currently authenticated user
        String userName = principal.getName();

        // Find the user object associated with the username
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        // Extract the user's id for the checkout process
        Order order = orderService.checkout(userId);

        // Return HTTP 201 created along with the newly created order 
        return ResponseEntity.status(201).body(order);
    }
}
