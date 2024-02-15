package com.lucifer.electronics.store.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private int cartItemId;

    private ProductDto productDto;

    private int itemQuantity;

    private int totalCartPrice;

}
