package com.bidy.auction.domain;

import com.bidy.home.domain.Product;
import jakarta.persistence.*;

/*
 * ItemTag Table 작성(Product와 Tag 연결 m : n)
 * JPA 활용
 * 외래키 (Product, Tag)
 */
@Entity(name = "item_tag")
public class ItemTag {
    // 1. product_tag_id: LONG, PK, Auto Increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productTagId;

    // 2. item_id: INTEGER, FK(Product -> product_id), Not Null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Product product;

    // 3. tag_id: LONG, FK(Tag -> tag_id), Not Null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
}
