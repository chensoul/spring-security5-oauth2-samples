package com.chensoul.oauth;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableOAuth2Sso
@EnableWebSecurity
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class ClientSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests().antMatchers("/").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().disable()
            .logout(logoutCustomizer -> logoutCustomizer.logoutSuccessUrl("/"));
  }
}
