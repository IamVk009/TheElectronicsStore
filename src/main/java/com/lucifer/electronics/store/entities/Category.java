package com.lucifer.electronics.store.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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
}
