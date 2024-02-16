package com.lucifer.electronics.store.services.impl;

import com.lucifer.electronics.store.dtos.AddItemToCartRequest;
import com.lucifer.electronics.store.dtos.CartDto;
import com.lucifer.electronics.store.entities.Cart;
import com.lucifer.electronics.store.entities.CartItem;
import com.lucifer.electronics.store.entities.Product;
import com.lucifer.electronics.store.entities.User;
import com.lucifer.electronics.store.exceptions.ResourceNotFoundException;
import com.lucifer.electronics.store.repositories.CartRepository;
import com.lucifer.electronics.store.repositories.ProductRepository;
import com.lucifer.electronics.store.repositories.UserRepository;
import com.lucifer.electronics.store.services.CartService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    ModelMapper mapper;

//    Method 1 : Add items to Cart
//    Case 1 - : If Cart for the user already exists, add the item to that cart.
//    Case 2 - : If Cart doesn't exist, then create the new cart and add item to it.

    /**
     * @param userId:  In order to check if Cart already exists for the particular user, we need to fetch user from DB using userId.
     * @param request: In order to add an item to the cart we need two things -
     *                 1. The product we want to add.(product Id)
     *                 2. The quantity of the product.
     *                 For that purpose we are passing new AddItemToCartRequest object as a param which contains the required fields.
     * @return CartDto
     */
    @Override
    public CartDto addItemsToCart(String userId, AddItemToCartRequest request) {
//      Extracting request params from AddItemToCartRequest.
        String productId = request.getProductId();
        int quantity = request.getQuantity();
//      Fetching product from DB using productId.
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Produt with given Id does not exists..!"));
//      Fetching user from DB using userId.
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given Id doesn't exists..!"));

        Cart cart = null;

        try {
//          Checking whether cart exists for fetched user.
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException exception) {
            log.info("Cart does not exist for the user with Id : {} ", userId + ", Hence creating new cart...");
            cart = new Cart();
//          Setting cartId manually since its a newly created cart and cartId is of type String
            cart.setCartId(UUID.randomUUID().toString());
//          Setting created date to cart.
            cart.setCreatedDate(new Date());
        }

        AtomicBoolean updated = new AtomicBoolean(false);
//      If item which we are going to add in the cart already exists, then increase the number of items and totalCartPrice of the cart.
        List<CartItem> cartItemList = cart.getCartItemList();
        List<CartItem> updatedCartItemList = cartItemList.stream().map(cartItem -> {
            if (cartItem.getProduct().getProductId().equals(productId)) {
                cartItem.setItemQuantity(quantity);
                cartItem.setTotalCartPrice(quantity * product.getPrice());
                updated.set(true);
            }
            return cartItem;
        }).toList();

//      Setting updatedItemsList to the cart.
        cart.setCartItemList(updatedCartItemList);

//      If Cart is New -  Performing operation on cart --------------------

        if (!updated.get()) {
//          Creating new cart item to add it to CartItemList
            CartItem cartItem = CartItem.builder()
                    .product(product)
                    .itemQuantity(quantity)
                    .totalCartPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .build();

//          Adding above cart to cartItemList.
            cart.getCartItemList().add(cartItem);
        }

//      Since its a new cart, we need to set user to this cart.
        cart.setUser(user);

//      Saving the card to DB
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart, CartDto.class);
    }
}