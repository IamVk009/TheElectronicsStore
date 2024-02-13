package com.lucifer.electronics.store.repositories;

import com.lucifer.electronics.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findByProductTitleContaining(String title, Pageable pageable);

    Page<Product> findByLiveTrue(Pageable pageable);
}
