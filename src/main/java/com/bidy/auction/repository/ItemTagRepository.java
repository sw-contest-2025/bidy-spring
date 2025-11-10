package com.bidy.auction.repository;

import com.bidy.auction.domain.ItemTag;
import com.bidy.post.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface ItemTagRepository  extends JpaRepository<ItemTag,Long> {
    // 특정 상품과 연결된 모든 Tag ID를 조회
    @Query("SELECT it.tag.tagId FROM ItemTag it WHERE it.product.productId = :productId")
    List<Long> findTagIdsByProductId(@Param("productId") Long productId);

    // 주어진 Tag ID를 가지며 특정 상품 ID는 제외하는 상품을 조회
    @Query("SELECT DISTINCT it.product FROM ItemTag it WHERE it.tag.tagId IN :tagIds AND it.product.productId != :excludedProductId ORDER BY it.product.createdAt DESC")
    List<Product> findProductsByTagIdsExcludingProduct(
            @Param("tagIds") List<Long> tagIds,
            @Param("excludedProductId") Long excludedProductId,
            Pageable pageable
    );

}
