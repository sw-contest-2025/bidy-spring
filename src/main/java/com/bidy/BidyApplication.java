package com.bidy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.bidy.member.domain", "com.bidy.home.domain", "com.bidy.auction.domain", "com.bidy.chat.entity"})
public class BidyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BidyApplication.class, args);
	}

}
