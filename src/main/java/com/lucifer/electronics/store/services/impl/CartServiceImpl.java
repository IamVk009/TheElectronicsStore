package com.lucifer.electronics.store.services.impl;

import com.lucifer.electronics.store.dtos.AddItemToCartRequest;
import com.lucifer.electronics.store.dtos.CartDto;
import com.lucifer.electronics.store.entities.Cart;
import com.lucifer.electronics.store.entities.CartItem;
import com.lucifer.electronics.store.entities.Product;
import com.lucifer.electronics.store.entities.User;
import com.lucifer.electronics.store.exceptions.BadApiRequestException;
import com.lucifer.electronics.store.exceptions.ResourceNotFoundException;
import com.lucifer.electronics.store.repositories.CartItemRepository;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper mapper;

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

        if( quantity <= 0){
            throw  new BadApiRequestException("Quantity must be greater than 0");
        }
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
        log.info("Cart1 : {} ", cart.getUser());
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
//      If item which we are going to add in the cart already exists, then increase the number of items and totalCartPrice of the cart.
        List<CartItem> items = cart.getCartItemList();
        items = items.stream().map(item -> {

            if (item.getProduct().getProductId().equals(productId)) {
                item.setItemQuantity(quantity);
                item.setTotalCartPrice(quantity * product.getDiscountedPrice());
                updated.set(true);
            }

            return item;
        }).collect(Collectors.toList());
        log.info("UpdatedItemList = {} ", items);
//      Setting updatedItemsList to the cart.
//      cart.setCartItemList(updatedItemList);
        log.info("Cart2 = {} ", cart);
//      If Cart is New -  Performing operation on cart --------------------

        if (!updated.get()) {
//          Creating new cart item to add it to CartItemList
            CartItem cartItem = CartItem.builder()
                    .product(product)
                    .itemQuantity(quantity)
                    .totalCartPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .build();
            log.info("CartItem : {} ", cartItem);
//          Adding above cartItem to cartItemList.
            cart.getCartItemList().add(cartItem);
        }
        log.info("Cart 3 : {} ", cart);
        log.info("User : {} ", user);
//      Since its a new cart, we need to set user to this cart.
        cart.setUser(user);
        log.info("Cart 4: {} ", cart.getUser());
//      Saving the card to DB
        Cart updatedCart = cartRepository.save(cart);
        log.info("Cart 5 : {} ", updatedCart);
//        List<CartItem> cartItemList1 = updatedCart.getCartItemList();
//        log.info("cartItemList : {}", cartItemList1);
        CartDto mapped = mapper.map(updatedCart, CartDto.class);
        log.info("Mapped : {}", mapped);
        return mapper.map(updatedCart, CartDto.class);
    }

    //  Method 2 : Delete Item from Cart
    @Override
    public void deleteItemFromCart(int cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("CartItem with given Id does not exist..!"));
        cartItemRepository.delete(cartItem);
    }

    //  Method 3 : Clear all items from Cart
    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given Id does not exist.."));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("This User does not have any associated Cart"));
        List<CartItem> cartItemList = cart.getCartItemList();
        cartItemList.clear();
        cartRepository.save(cart);
    }

    //  Method 4 : Fetch cart of particular user
    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given Id does not exist.."));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("This User does not have any associated Cart"));
        return mapper.map(cart, CartDto.class);
    }
}