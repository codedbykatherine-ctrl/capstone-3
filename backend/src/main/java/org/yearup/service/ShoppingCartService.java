package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart addProduct(int userId, int productId){
        Product product = productService.getById(productId);
        if (product == null){
            return null;
        }
        CartItem cartItem = shoppingCartRepository.findByUserIdAndProductId(userId,productId);
        if (cartItem == null){
            CartItem newCartItem = new CartItem();
            newCartItem.setUserId(userId);
            newCartItem.setProductId(productId);
            newCartItem.setQuantity(1);
            shoppingCartRepository.save(newCartItem);
        }else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            shoppingCartRepository.save(cartItem);
        }
        return getByUserId(userId);
    }

    public ShoppingCart getByUserId(int userId)
    {
        ShoppingCart shoppingCart = new ShoppingCart();
        List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);
        for (CartItem cartItem: cartItems){
            Product product = productService.getById(cartItem.getProductId());
            ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(cartItem.getQuantity());
            shoppingCart.add(shoppingCartItem);
        }
        // load the user's cart rows, look up each product, and build the ShoppingCart
        return shoppingCart;
    }

    // add additional methods here
    public ShoppingCart updateShoppingCart( int userId, int productId, ShoppingCartItem shoppingCartItem){

        CartItem cartItem = shoppingCartRepository.findByUserIdAndProductId(userId,productId);
        if (cartItem == null){
            return null;
        }

       // ShoppingCartItem updatedShoppingCartItem = getByUserId(userId).get(productId);
       // updatedShoppingCartItem.setQuantity(shoppingCartItem.getQuantity());

        cartItem.setQuantity(shoppingCartItem.getQuantity());

        shoppingCartRepository.save(cartItem);

        return getByUserId(userId);
    }

    @Transactional
    public ShoppingCart deleteProducts(int userId){
        shoppingCartRepository.deleteByUserId(userId);
        return getByUserId(userId);
    }

}
