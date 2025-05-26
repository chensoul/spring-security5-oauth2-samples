

package com.chensoul.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Override
  @Bean(BeanIds.AUTHENTICATION_MANAGER)
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  @Bean
  public UserDetailsService userDetailsService() {
    return new InMemoryUserDetailsManager(
            User.withDefaultPasswordEncoder().username("user").password("pass").roles("USER").build());
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // @formatter:off
		auth.authenticationEventPublisher(new AuthenticationEventPublisher() {
			@Override
			public void publishAuthenticationSuccess(Authentication authentication) {
				log.info("Token request by password grant succeeded.");
			}

			@Override
			public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
				log.info("Token request by password grant failed: {}", exception);
			}
		}).userDetailsService(userDetailsService());
		// @formatter:on
  }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }


}
