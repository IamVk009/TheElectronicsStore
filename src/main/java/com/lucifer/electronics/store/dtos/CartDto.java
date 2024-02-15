package com.lucifer.electronics.store.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {

    private String cartId;

    private Date createdDate;

    private UserDto userDto;

    List<CartItemDto> cartItemDtoList = new ArrayList<>();
}