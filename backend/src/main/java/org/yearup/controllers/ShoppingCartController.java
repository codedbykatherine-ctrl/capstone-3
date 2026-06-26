package org.yearup.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;

// convert this class into a REST controller that handles shopping cart requests
// map all endpoints to /cart
//only authenticated users with the USER role can access these endpoints
//allow requests from the frontend application
@RestController
@RequestMapping("/cart")
@PreAuthorize("hasRole('ROLE_USER')")
@CrossOrigin
public class ShoppingCartController {
    // a shopping cart controller depends on the service la
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;

    }


    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    public ShoppingCart getCart(Principal principal) {
        // get the currently logged-in username
        String userName = principal.getName();
        // find database user by username
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        // use the shoppingCartService to get all items in the cart and return the cart
        return shoppingCartService.getByUserId(userId);
    }

    // add a product to the logged in user's shopping cart
    @PostMapping("/products/{productId}")
    //Method returns ResponseEntity containing a shoppingCart
    // Response Entity lets ypu control the status code and the response body
    public ResponseEntity<ShoppingCart> addProductToCart(@PathVariable int productId, Principal principal) {
        // get the username of the currently logged in user
        String username = principal.getName();
        // use the username to find the user's database id
        int userId = userService.getIdByUsername(username);
        // add the selected product to the user's cart and return the updated cart
        ShoppingCart updatedCart = shoppingCartService.addProduct(userId, productId);
        // return 201 created with the updated shopping cart
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15  (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated; return the cart (200 OK)
    @PutMapping("/products/{productId}")
    public ResponseEntity<ShoppingCart> updateProduct(@PathVariable int productId, @RequestBody ShoppingCartItem shoppingCartItem, Principal principal) {
        String username = principal.getName();
        int userId = userService.getIdByUsername(username);

        ShoppingCart shoppingCart = shoppingCartService.updateShoppingCart(userId, productId, shoppingCartItem);
        if (shoppingCart == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(shoppingCart);
    }


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart  - return the (now empty) cart so the front end can refresh it (200 OK)
    //Maps this method tp HTTP DELETE requests
    @DeleteMapping
    // The endpoint removes all products from the logged-in user's cart
    public ResponseEntity<ShoppingCart> deleteProducts(Principal principal) {
        // Get the username of the currently authenticated user
        String username = principal.getName();
        // FiND THE User object that belongs to the logged-in username
        User user = userService.getByUserName(username);
        //Remove all products from the user's shopping cart
        shoppingCartService.deleteProducts(user.getId());
        // Retrieve the now empty shopping cart after deletion
        ShoppingCart cart = shoppingCartService.getByUserId(user.getId());
        //Return HTTP 200 ok along with the updated(empty)shopping cart.
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }
}
