package com.bidy.auction.domain;

import jakarta.persistence.*;

/*
 * Tag Table 작성(태그 보관)
 * JPA 활용
 */
@Entity
@Table(name = "tag")
public class Tag {
    // 1. tag_id: LONG, PK, Auto Increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    // 2. tagName: VARCHAR(50), Unique, Not Null
    @Column(
            length = 50,
            unique = true,
            nullable = false
    )
    private String tagName;
}
