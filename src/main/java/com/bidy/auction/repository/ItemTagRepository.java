package com.bidy.auction.repository;

import com.bidy.auction.domain.ItemTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemTagRepository  extends JpaRepository<ItemTag,Long> {
}
