
package com.chensoul.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.stereotype.Component;

@Component
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    // @formatter:off
		clients.inMemory().withClient("client").authorizedGrantTypes("password")
		.secret(passwordEncoder.encode("secret")).scopes("read","write");
		// @formatter:on
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    // @formatter:off
		endpoints.authenticationManager(this.authenticationManager);
		// @formatter:on
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    // @formatter:off
		security.checkTokenAccess("isAuthenticated()");
		// @formatter:on
  }

}
