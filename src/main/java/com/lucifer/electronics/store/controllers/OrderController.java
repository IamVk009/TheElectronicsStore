package com.lucifer.electronics.store.controllers;

import com.lucifer.electronics.store.dtos.*;
import com.lucifer.electronics.store.services.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@Tag(name = "Order Controller", description = "REST APIs to perform Order Related operations")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        OrderDto order = orderService.createOrder(createOrderRequest);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
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
                                                                   @RequestParam(defaultValue = "desc") String sortDirection) {

        PageableResponse<OrderDto> allOrders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDirection);
        return new ResponseEntity<>(allOrders, HttpStatus.OK);
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable String orderId, @RequestBody UpdateOrderRequest updateOrderRequest){
        OrderDto updateOrder = orderService.updateOrder(orderId, updateOrderRequest);
        return new ResponseEntity<>(updateOrder, HttpStatus.OK);
    }
}
