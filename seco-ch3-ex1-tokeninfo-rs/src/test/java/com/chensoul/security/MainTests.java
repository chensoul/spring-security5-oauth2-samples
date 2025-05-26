package com.chensoul.security;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {

    @Autowired
    private MockMvc mockMvc;

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void init() {
        wireMockServer = new WireMockServer(new WireMockConfiguration().port(8080));
        wireMockServer.start();
        WireMock.configureFor("localhost", 8080);
    }

    @Test
        public void testAccessTokenIsObtainedUsingValidClientCredentials() throws Exception {
        String token = UUID.randomUUID().toString();

        stubFor(WireMock.post(urlMatching("/oauth/check_token"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(OK.value())
                        .withBody("{\"active\":true,\"exp\":1587946446,\"user_name\":\"user\",\"authorities\":[\"read\"],\"client_id\":\"client\",\"scope\":[\"read\"]}")));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/hello")
                                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }
}
