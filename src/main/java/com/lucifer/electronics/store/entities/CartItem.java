package com.lucifer.electronics.store.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * This entity class will be used to store information -
 * 1. Which entity(product) is getting stored in which cart ?
 * 2. In what quantity, the item is getting stored in particular cart ?
 * 3. Total cost of the cart ?
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;

//  Establishing OntToOne relationship between CartItem entity and Product entity.
//  When CartItem is persisted or updated in the DB, the value of this foreign key column (product_id) will be set to the primary key value of the associated Product entity.
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int itemQuantity;

    private int totalCartPrice;

//  Establishing ManyToOne relationship between CartItem entity and Cart entity.
//  With FetchType.LAZY, the associated Cart entity will be lazily fetched, i.e. it will be loaded from the database only when accessed for the first time.
//  This can improve performance by avoiding unnecessary database queries.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;
}