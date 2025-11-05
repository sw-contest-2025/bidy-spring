package com.bidy.auction.domain;

import com.bidy.home.domain.Member;
import com.bidy.home.domain.Product;
import jakarta.persistence.*;

import java.time.LocalDateTime;
/*
* Bid Table 작성(입찰 기록 보관)
* JPA 활용
* 외래키 (Product, Member)
 */
@Entity(name = "bid")
public class Bid {
    // 1. bid_id: LONG, PK, Auto Increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    // 2. item_id: INTEGER, FK(Product -> product_id), Not NULL
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Product product;

    // 3. bidder_id: LONG, FK(Member -> member_id), Not Null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidder_id", nullable = false)
    private Member bidder;

    // 4. bid_price: INTEGER, Not Null
    @Column(nullable = false)
    private int bidPrice;

    // 5. bid_time: DATETIME, Not Null
    @Column(nullable = false)
    private LocalDateTime bidTime;
}
