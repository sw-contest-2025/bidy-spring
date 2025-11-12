package com.bidy.auction.service;

import com.bidy.auction.domain.Bid;
import com.bidy.auction.repository.BidRepository;
import com.bidy.post.domain.Product;
import com.bidy.post.repository.PostProductRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AuctionScheduler {
    private final PostProductRepository productRepository;
    private final BidRepository bidRepository;
    private final AuctionService auctionService;

    public AuctionScheduler(
            PostProductRepository productRepository,
            BidRepository bidRepository,
            AuctionService auctionService)
    {
        this.productRepository = productRepository;
        this.bidRepository = bidRepository;
        this.auctionService = auctionService;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkAuctionEndings()
    {
        List<Product>  products = productRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for(Product product : products){
            LocalDateTime endTime = product.calculateEndTime();
            if(endTime != null && endTime.isBefore(now) && !product.isEnded()){
                auctionService.finishAuction(product.getProductId());
            }
        }
    }
}