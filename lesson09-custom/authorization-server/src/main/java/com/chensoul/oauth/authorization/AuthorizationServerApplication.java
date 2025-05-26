package com.chensoul.oauth.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class that starts the Spring Boot application.
 */
@SpringBootApplication(scanBasePackages = {"com.chensoul.oauth.authorization",
	"com.chensoul.oauth.common"})
public class AuthorizationServerApplication {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}

}
