package com.bidy.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BidRequestDto {
    // 입찰 대상 상품 ID
    private Long productId;
    // 사용자가 입력한 입찰 가격
    private int bidPrice;
    // 입찰자 ID
    private Long bidderId;
}
