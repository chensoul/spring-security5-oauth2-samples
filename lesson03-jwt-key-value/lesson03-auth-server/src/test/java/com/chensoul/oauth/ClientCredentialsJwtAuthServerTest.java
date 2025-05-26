

package com.chensoul.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignerVerifier;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests confirming behavior of minimally-configured JWT-encoding Authorization Server
 *
 * @author Josh Cummings
 */
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ClientCredentialsJwtAuthServerTest {

  private static final String CLIENT_ID = "client";

  private static final String CLIENT_SECRET = "secret";

  private static final RequestPostProcessor CLIENT_CREDENTIALS = httpBasic(CLIENT_ID, CLIENT_SECRET);

  @Value("${security.oauth2.authorization.jwt.key-value}")
  String privateKeyValue;

  @Autowired
  MockMvc mvc;

  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void tokenWhenUsingClientCredentialsThenIsValid() throws Exception {
    MvcResult result = this.mvc.perform(post("/oauth/token").with(CLIENT_CREDENTIALS)
                    .param("grant_type", "client_credentials").param("scope", "read")).andExpect(status().isOk())
            .andReturn();

    String accessToken = extract(result, "access_token");

    SignerVerifier signerVerifier = privateKeyValue.startsWith("-----BEGIN") ? (SignerVerifier) new RsaVerifier(privateKeyValue) : new MacSigner(privateKeyValue);
    JwtHelper.decodeAndVerify(accessToken, signerVerifier);
  }

  private String extract(MvcResult result, String property) throws Exception {
    return this.objectMapper.readValue(result.getResponse().getContentAsString(), Map.class).get(property)
            .toString();
  }

}
