package com.lucifer.electronics.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "cat_id")
    private String categoryId;

    @Column(name = "cat_title", unique = true, nullable = false)
    private String title;

    @Column(name = "cat_desc")
    private String description;

    @Column(name = "cat_coverImg", nullable = false)
    private String coverImage;
    /**
     * 1. mappedBy = "category": This attribute specifies the field in the Product entity that maps this relationship.
     * It refers to the category field in the Product entity.
     *
     * 2. fetch = FetchType.LAZY: This attribute specifies the fetch strategy for retrieving associated entities.
     * In this case, it indicates that associated Product entities should be loaded lazily, meaning they will be
     * fetched from the database only when accessed for the first time.
     *
     * 3. cascade = CascadeType.ALL: This attribute specifies the cascade behavior for operations such as persist,
     * merge, remove, refresh, and detach.In this case, if a Category entity is deleted,
     * all associated Product entities will also be deleted.
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> productList = new ArrayList<>();
}
