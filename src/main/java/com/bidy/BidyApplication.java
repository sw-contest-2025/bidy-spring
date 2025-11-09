package com.bidy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages = "com.bidy") // 모든 엔티티 스캔
@EnableScheduling
public class BidyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BidyApplication.class, args);
	}

}
