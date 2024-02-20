package com.lucifer.electronics.store.services;

import com.lucifer.electronics.store.dtos.OrderDto;
import com.lucifer.electronics.store.dtos.PageableResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    OrderDto createOrder(String userId, OrderDto orderDto);

    void removeOrder(String orderId);

    List<OrderDto> getAllOrderOfUser(String userId);

    PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDirection);
}
