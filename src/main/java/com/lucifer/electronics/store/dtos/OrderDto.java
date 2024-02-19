package com.lucifer.electronics.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private String orderId;

    private String orderStatus = "PENDING";

    private String paymentStatus = "NOT_PAID";

    private int orderAmount;

    private String billingAddress;

    private String billingPhone;

    private String billingName;

    private Date orderDate = new Date();

    private Date deliveredDate;

    private UserDto user;

    private List<OrderItemDto> orderItems = new ArrayList<>();
}
