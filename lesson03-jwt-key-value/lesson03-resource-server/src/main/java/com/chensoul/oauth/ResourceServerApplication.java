

package com.chensoul.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@SpringBootApplication
@EnableResourceServer
public class ResourceServerApplication extends ResourceServerConfigurerAdapter {

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.antMatcher("/secured/**").authorizeRequests().anyRequest().authenticated();
  }

  public static void main(String[] args) {
    SpringApplication.run(ResourceServerApplication.class, args);
  }

}
