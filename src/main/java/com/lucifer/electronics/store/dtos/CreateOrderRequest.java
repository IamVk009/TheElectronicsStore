package com.lucifer.electronics.store.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

    private String userId;

    private String cartId;

    private String orderStatus = "PENDING";

    private String paymentStatus = "NOT_PAID";

    private String billingAddress;

    private String billingPhone;

    private String billingName;

    private Date deliveredDate = null;
}
