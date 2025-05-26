

package com.chensoul.oauth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Series of automated integration tests to verify proper behavior of JWT-encoded Bearer
 * Token-secured Resource Server
 *
 * @author Josh Cummings
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ResourceServerApplicationTests {

  private static final String VALID_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJyZWFkIl0sImV4cCI6MTcyNzEyMjA2OCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6InBCRTZFQUplR1p4RHRmQVd6QWFtTHRGZGQ2NCIsImNsaWVudF9pZCI6ImNsaWVudCJ9.4iMP4-Kj-9vH7tqbHNLPwRWthPjqG3xlNjho3bhQRUM";

  //TODO: 改为过期的 token
  private static final String EXPIRED_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJyZWFkIl0sImV4cCI6MTcyNzEyMjA2OCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6InBCRTZFQUplR1p4RHRmQVd6QWFtTHRGZGQ2NCIsImNsaWVudF9pZCI6ImNsaWVudCJ9.4iMP4-Kj-9vH7tqbHNLPwRWthPjqG3xlNjho3bhQRUM";

  @Autowired
  MockMvc mvc;

  @Test
  public void homePageAvailable() throws Exception {
    this.mvc.perform(get("/").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
  }

  @Test
  public void securedWhenUsingValidJwtThenOk() throws Exception {
    this.mvc.perform(get("/secured/1").with(bearerToken(VALID_JWT))).andExpect(status().isOk());
    this.mvc.perform(get("/secured/2").with(bearerToken(VALID_JWT))).andExpect(status().isOk());
  }

  @Test
  public void ecuredWhenUsingExpiredJwtThenUnauthorized() throws Exception {
    this.mvc.perform(get("/secured/1").with(bearerToken(EXPIRED_JWT))).andExpect(status().isUnauthorized());
    this.mvc.perform(get("/secured/2").with(bearerToken(EXPIRED_JWT))).andExpect(status().isUnauthorized());
  }

  private static class BearerTokenRequestPostProcessor implements RequestPostProcessor {

    private String token;

    public BearerTokenRequestPostProcessor(String token) {
      this.token = token;
    }

    @Override
    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
      request.addHeader("Authorization", "Bearer " + this.token);
      return request;
    }

  }

  private static BearerTokenRequestPostProcessor bearerToken(String token) {
    return new BearerTokenRequestPostProcessor(token);
  }

}
