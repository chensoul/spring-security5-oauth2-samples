package com.chensoul.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {
    @Value("${jwt.key-uri}")
    private String keySetUrl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(
                c -> c.jwt(jwt -> jwt.jwkSetUri(keySetUrl))
        );

        http.authorizeRequests().anyRequest().authenticated();
    }
}
