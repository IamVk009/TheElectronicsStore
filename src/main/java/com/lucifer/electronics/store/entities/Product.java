package com.lucifer.electronics.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "prod_id")
    private String productId;

    private String productTitle;

    @Column(name = "prod_desc", length = 1000)
    private String description;

    private int price;

    private int discountedPrice;

    private int quantity;

    private Date addedDate;

    private boolean live;

    private boolean stock;

    private String imageName;

    /**
     * 1. @ManyToOne: This annotation indicates that the associated entity (in this case, the Product entity) belongs
     * to a many-to-one relationship with the Category entity. This means that many products can belong to one category.
     *
     * 2. @JoinColumn(name = "category_id"): It indicates that the category_id column in the Product table is used
     * as the foreign key to reference the Category table.
     *
     * 3. fetch = FetchType.EAGER: This attribute specifies the fetch strategy for loading the associated Category entity.
     * In this case, EAGER fetching is used, which means that the Category entity will be loaded at the same time as
     * the owning Product entity whenever the Product entity is retrieved.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
}
