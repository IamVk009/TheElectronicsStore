package com.lucifer.electronics.store.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(name = "prod_title")
    private String productTitle;

    @Column(name = "prod_desc", length = 1000)
    private String description;

    @Column(name = "prod_price")
    private int price;

    @Column(name = "prod_dis_price")
    private int discountedPrice;

    @Column(name = "prod_qtty")
    private int quantity;

    @Column(name = "prod_date")
    private Date addedDate;

    @Column(name = "prod_live")
    private boolean isLive;

    @Column(name = "prod_stock")
    private boolean isInStock;
}
