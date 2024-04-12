package com.lucifer.electronics.store.controllers;

import com.lucifer.electronics.store.dtos.AddItemToCartRequest;
import com.lucifer.electronics.store.dtos.ApiResponseMessage;
import com.lucifer.electronics.store.dtos.CartDto;
import com.lucifer.electronics.store.services.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/cart")
@Tag(name = "Cart Controller", description = "REST APIs to perform Cart Related Operations")
public class CartConroller {

    @Autowired
    CartService cartService;

    @PostMapping("/add/{userId}")
    public ResponseEntity<CartDto> addItemsToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request) {
        CartDto cartDto = cartService.addItemsToCart(userId, request);
        log.info("CartDto from controller : " + cartDto);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @PutMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponseMessage> deleteItemFromCart(@PathVariable int cartItemId) {
        cartService.deleteItemFromCart(cartItemId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Item deleted from cart successfully...")
                .success(true)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/clear/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Cart is clear, you can start adding items to the cart.")
                .success(true)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId) {
        CartDto cartOfUser = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartOfUser, HttpStatus.OK);
    }
}
