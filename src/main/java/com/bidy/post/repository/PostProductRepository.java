package com.bidy.post.repository;

import com.bidy.post.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByUser_MemberId(Long memberId);

    List<Product> findByWinner_MemberId(Long memberId);

    List<Product> findByCategory(String category);
}
