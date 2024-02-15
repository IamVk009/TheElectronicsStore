package com.lucifer.electronics.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This entity class will be used to store the information -
 * 1. Which user has created which cart ? / Who is the owner of this cart ?
 * 2. Created Date of the cart ?
 * 3.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    private String cartId;

    private Date createdDate;

//  Establishing one-to-one relationship between the current entity Cart and another entity User.
//  In the database schema, this relationship typically results in a foreign key column in the table of Cart referencing the primary key of the User table.
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

//  Establishing one-to-many relationship between the current entity Cart and another entity CartItem.
//  Using mappedBy attribute, specifying that the field 'cart' in the target entity (CartItem) is being used to map this OneToMany relationship.
//  With CascadeType.ALL, operations such as persisting, merging, or removing the current entity will be cascaded to the associated CartItem entities.
//  So, If a Cart entity is deleted, all associated CartItem entities will also be deleted.
//  With FetchType.EAGER, the associated CartItem entities will be eagerly fetched along with the current entity.
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<CartItem> cartItemList = new ArrayList<>();
}