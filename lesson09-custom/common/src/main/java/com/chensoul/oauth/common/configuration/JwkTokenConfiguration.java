package com.chensoul.oauth.common.configuration;

import com.chensoul.oauth.common.support.JwkKeyPairAccessTokenConverter;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @see OAuth2ResourceServerConfiguration
 * @see ResourceServerTokenServicesConfiguration
 */
@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(prefix = "security.oauth2", name = "token-type", havingValue = "jwk")
public class JwkTokenConfiguration {
  private static final String JWK_KID = new RandomValueStringGenerator().generate();
  private final AuthorizationServerProperties properties;
  private final ApplicationContext context;

  @Bean
  public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
    return new JwtTokenStore(jwtAccessTokenConverter);
  }

  @Bean
  public KeyPair keyPair() {
    Resource keyStore = this.context.getResource(properties.getJwt().getKeyStore());
    KeyStoreKeyFactory ksFactory = new KeyStoreKeyFactory(keyStore,
            properties.getJwt().getKeyStorePassword().toCharArray());

    if (!StringUtils.isEmpty(properties.getJwt().getKeyPassword())) {
      return ksFactory.getKeyPair(properties.getJwt().getKeyAlias(),
              properties.getJwt().getKeyPassword().toCharArray());
    }
    return ksFactory.getKeyPair(properties.getJwt().getKeyAlias());
  }

  @Bean
  public JWKSet jwkSet() {
    RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) keyPair().getPublic());
    builder.keyUse(KeyUse.SIGNATURE).algorithm(JWSAlgorithm.RS256).keyID(JWK_KID);
    return new JWKSet(builder.build());
  }

  @Bean
  public JwtAccessTokenConverter jwtAccessTokenConverter() {
    return new JwkKeyPairAccessTokenConverter(keyPair(), Collections.singletonMap("kid", JWK_KID));
  }

  @Order(-1)
  @Configuration
  @AutoConfigureAfter(JwkTokenConfiguration.class)
  public class JwkSetEndpointConfiguration extends AuthorizationServerSecurityConfiguration {
    /**
     * @param http the {@link HttpSecurity} to modify for enabling the endpoint.
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests().antMatchers("/endpoint/jwks.json").permitAll()
              .and().requestMatchers().antMatchers("/endpoint/jwks.json");
      super.configure(http);
    }

    @RequiredArgsConstructor
    @FrameworkEndpoint
    public class JwkSetEndpoint {
      private final JWKSet jwkSet;

      @GetMapping("/endpoint/jwks.json")
      @ResponseBody
      public Map<String, Object> jwks() {
        return this.jwkSet.toJSONObject();
      }
    }
  }
}
