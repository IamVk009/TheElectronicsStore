package com.lucifer.electronics.store.controllers;

import com.lucifer.electronics.store.dtos.ApiResponseMessage;
import com.lucifer.electronics.store.dtos.CreateOrderRequest;
import com.lucifer.electronics.store.dtos.OrderDto;
import com.lucifer.electronics.store.dtos.PageableResponse;
import com.lucifer.electronics.store.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        OrderDto order = orderService.createOrder(createOrderRequest);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId) {
        orderService.removeOrder(orderId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Order removed successfully...!")
                .success(true)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getAllOrdersOfUser(@PathVariable String userId) {
        List<OrderDto> allOrdersOfUser = orderService.getAllOrdersOfUser(userId);
        return new ResponseEntity<>(allOrdersOfUser, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(@RequestParam(defaultValue = "0") int pageNumber,
                                                                   @RequestParam(defaultValue = "3") int pageSize,
                                                                   @RequestParam(defaultValue = "orderId") String sortBy,
                                                                   @RequestParam(defaultValue = "asc") String sortDirection) {

        PageableResponse<OrderDto> allOrders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(allOrders, HttpStatus.OK);
    }
}
