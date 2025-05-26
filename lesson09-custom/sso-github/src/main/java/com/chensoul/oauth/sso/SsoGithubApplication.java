package com.chensoul.oauth.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * TODO
 *
 * @author <a href="mailto:chensoul.eth@gmail.com">chensoul</a>
 * @since 1.0.0
 */
@SpringBootApplication
@EnableOAuth2Sso
public class SsoGithubApplication implements WebMvcConfigurer {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SsoGithubApplication.class, args);
	}

}
