package com.lucifer.electronics.store.repositories;

import com.lucifer.electronics.store.entities.Order;
import com.lucifer.electronics.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUser(User user);
}
