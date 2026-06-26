package org.yearup.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.*;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;

import java.time.LocalDateTime;

@Service
public class OrderService {
    //Repository used to save and retrieve order records from the database
    private final OrderRepository orderRepository;
    //Repository used to save each product inside the order
    private final OrderLineItemRepository orderLineItemRepository;
    //Service used to access and clear teh user's shopping cart
    private final ShoppingCartService shoppingCartService;
    //Service used to retrieve the use's profile and shipping information
    private final ProfileService profileService;

    //Constructor injection gives this service access to the repositories
    public OrderService(OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository, ShoppingCartService shoppingCartService, ProfileService profileService) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.shoppingCartService = shoppingCartService;
        this.profileService = profileService;
    }
    //Creates an order from the user's current shopping cart
    public Order checkout(int userId) {
        // Retrieve the user's current shopping cart
        ShoppingCart cart = shoppingCartService.getByUserId(userId);

        // Prevent checkout if the cart does not contain any items
        if(cart.getItems().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cart is empty");
        }

        // GRetrieve the user's profile to use their saved shipping information
        Profile profile = profileService.getByUserId(userId);

        // Build a new order using the user id, current date, and profile address
        Order order = new Order();
        order.setUserId(userId);
        order.setDate(LocalDateTime.now());
        order.setAddress(profile.getAddress());
        order.setCity(profile.getCity());
        order.setState(profile.getState());
        order.setZip(profile.getZip());
        order.setShippingAmount(0);

       // Save the order first so the database generates an order id
        Order saveOrder = orderRepository.save(order);

        // Convert each shopping cart item into an order line item.
        for (ShoppingCartItem item : cart.getItems().values()) {
            OrderLineItem lineItem = new OrderLineItem();

            // Connect this line item to the saved order
            lineItem.setOrderId(saveOrder.getOrderId());
            // Store product, price, quantity, and discount details for the order
            lineItem.setProductId(item.getProduct().getProductId());
            lineItem.setSalesPrice(item.getProduct().getPrice());
            lineItem.setQuantity(item.getQuantity());
            lineItem.setDiscount(item.getDiscountPercent());
            // save the line item to the database
            orderLineItemRepository.save(lineItem);

        }
        // Empty the user's cart after checkout is complete.
        shoppingCartService.deleteProducts(userId);

        // Return the completed saved order.
        return saveOrder;
    }
}