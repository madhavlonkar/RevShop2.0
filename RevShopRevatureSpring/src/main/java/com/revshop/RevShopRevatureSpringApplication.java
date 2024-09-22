package com.revshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RevShopRevatureSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(RevShopRevatureSpringApplication.class, args);

	}
}
