package com.lucifer.electronics.store.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderRequest {

    private String orderStatus;

    private String paymentStatus;

    private Date deliveredDate;
}
