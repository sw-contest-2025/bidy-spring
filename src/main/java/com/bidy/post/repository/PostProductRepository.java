package com.bidy.post.repository;

import com.bidy.post.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostProductRepository extends JpaRepository<Product, Long> {
}
