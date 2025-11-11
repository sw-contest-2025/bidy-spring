package com.bidy.post.repository;

import com.bidy.post.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface PostProductRepository extends JpaRepository<Product, Long> {
    // 특정 ID들을 제외하고, 조회수 내림차순으로 상품 N개를 조회
    @Query("SELECT p FROM Product p WHERE p.productId NOT IN :excludedIds AND p.isEnded = false ORDER BY p.views DESC, p.productId DESC")
    List<Product> findByViewsExcludingIds(
            @Param("excludedIds") List<Long> excludedIds,
            Pageable pageable
    );

    List<Product> findByUser_MemberId(Long memberId);

    List<Product> findByWinner_MemberId(Long memberId);

    List<Product> findByCategory(String category);

    List<Product> findByPostNameContainingIgnoreCase(String keyword);

    List<Product> findByProductIdNotIn(List<Long> productIds, Pageable pageable);
}
