package com.chensoul.oauth.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.chensoul.oauth.resource", "com.chensoul.oauth.common"})
public class ResourceServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ResourceServerApplication.class, args);
	}
}
