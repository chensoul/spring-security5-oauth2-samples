package com.chensoul.oauth.common.configuration;

import com.chensoul.oauth.common.support.RandomAuthenticationKeyGenerator;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

/**
 * @see OAuth2ResourceServerConfiguration
 * @see ResourceServerTokenServicesConfiguration
 */
@Configuration
@ConditionalOnProperty(prefix = "security.oauth2", name = "token-type", havingValue = "jdbc")
public class JdbcTokenConfiguration {
	@Bean
	public TokenStore tokenStore(DataSource dataSource) {
		JdbcTokenStore jdbcTokenStore = new JdbcTokenStore(dataSource);
		jdbcTokenStore.setAuthenticationKeyGenerator(new RandomAuthenticationKeyGenerator());
		return jdbcTokenStore;
	}
}
