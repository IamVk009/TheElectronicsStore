package com.lucifer.electronics.store.services;

import com.lucifer.electronics.store.dtos.AddItemToCartRequest;
import com.lucifer.electronics.store.dtos.CartDto;

public interface CartService {

    CartDto addItemsToCart(String userId, AddItemToCartRequest request);

    void deleteItemFromCart(int cartItemId);

    void clearCart(String userId);
}
