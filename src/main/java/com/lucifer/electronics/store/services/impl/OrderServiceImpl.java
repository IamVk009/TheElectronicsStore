package com.lucifer.electronics.store.services.impl;

import com.lucifer.electronics.store.dtos.CreateOrderRequest;
import com.lucifer.electronics.store.dtos.OrderDto;
import com.lucifer.electronics.store.dtos.PageableResponse;
import com.lucifer.electronics.store.entities.*;
import com.lucifer.electronics.store.exceptions.BadApiRequestException;
import com.lucifer.electronics.store.exceptions.ResourceNotFoundException;
import com.lucifer.electronics.store.helper.Helper;
import com.lucifer.electronics.store.repositories.CartRepository;
import com.lucifer.electronics.store.repositories.OrderRepository;
import com.lucifer.electronics.store.repositories.UserRepository;
import com.lucifer.electronics.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public OrderDto createOrder(CreateOrderRequest createOrderRequest) {

        String userId = createOrderRequest.getUserId();
        String cartId = createOrderRequest.getCartId();

//      Fetching user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given Id does not exist.."));
//      Fetching Cart
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart with given Id does not exist."));
//      Get all items from the above cart
        List<CartItem> cartItemList = cart.getCartItemList();

        if (cartItemList.isEmpty())
            throw new BadApiRequestException("Cart fetched with given Id does not contain any product");

//      Creating new order
        Order order = Order.builder()
                .billingName(createOrderRequest.getBillingName())
                .billingPhone(createOrderRequest.getBillingPhone())
                .billingAddress(createOrderRequest.getBillingAddress())
                .orderDate(new Date())
                .deliveredDate(createOrderRequest.getDeliveredDate())
                .paymentStatus(createOrderRequest.getPaymentStatus())
                .orderStatus(createOrderRequest.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        AtomicReference<Integer> totalOrderAmount = new AtomicReference<>(0);
//      Map cartItemList (fetched at line 49) to orderItemList (Transfer all cart items present in cartItemList into orderItemList)
        List<OrderItem> orderItemList = cartItemList.stream().map(cartitem -> {
            OrderItem orderItem = OrderItem.builder()
                    .product(cartitem.getProduct())
                    .quantity(cartitem.getItemQuantity())
                    .totalPrice(cartitem.getItemQuantity() * cartitem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();

//          Calculating tatal amount of all orderItems
            totalOrderAmount.set(totalOrderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).toList();

//      Setting above orderitemList into created order
        order.setOrderItems(orderItemList);
//      Setting total orderAmount into created order
        order.setOrderAmount(totalOrderAmount.get());

//      Since all the cartItems has been transferred to orderItemsList now we need to clear that particular cart and save it.
        cart.getCartItemList().clear();
        cartRepository.save(cart);

//      Save the newly created order
        Order createdOrder = orderRepository.save(order);
        return mapper.map(createdOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order with given Id does not exist.."));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getAllOrdersOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id does not exist.."));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtoList = orders.stream().map(order -> mapper.map(order, OrderDto.class)).toList();
        return orderDtoList;
    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = (sortDirection.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> orders = orderRepository.findAll(pageable);
        return Helper.getPageableResponse(orders, OrderDto.class);
    }
}
