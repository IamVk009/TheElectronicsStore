package com.lucifer.electronics.store.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

    @NotBlank(message = "User id is required..")
    private String userId;

    @NotBlank(message = "Please enter Cart Id")
    private String cartId;

    private String orderStatus = "PENDING";

    private String paymentStatus = "NOT_PAID";

    @NotBlank(message = "Please enter billing Address")
    private String billingAddress;

    @NotBlank(message = "Please enter billing Phone Number")
    private String billingPhone;

    @NotBlank(message = "Please enter billing Name")
    private String billingName;

    private Date deliveredDate = null;
}
