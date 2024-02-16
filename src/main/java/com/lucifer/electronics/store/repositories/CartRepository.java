package com.lucifer.electronics.store.repositories;

import com.lucifer.electronics.store.entities.Cart;
import com.lucifer.electronics.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser(User user);
}
