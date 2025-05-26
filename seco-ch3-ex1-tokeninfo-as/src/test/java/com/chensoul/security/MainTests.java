package com.chensoul.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {

    @Autowired
    private MockMvc mvc;

    @Test
        public void testAccessTokenIsObtainedUsingValidUserAndClient() throws Exception {
        mvc.perform(
                        post("/oauth/token")
                                .with(httpBasic("client", "secret"))
                                .queryParam("grant_type", "password")
                                .queryParam("username", "user")
                                .queryParam("password", "pass")
                                .queryParam("scope", "read")
                )
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(status().isOk());
    }

    @Test
        public void testCheckTokenEndpoint() throws Exception {
        String content =
                mvc.perform(
                                post("/oauth/token")
                                        .with(httpBasic("client", "secret"))
                                        .queryParam("grant_type", "password")
                                        .queryParam("username", "user")
                                        .queryParam("password", "pass")
                                        .queryParam("scope", "read")
                        )
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(content, Map.class);

        mvc.perform(
                        post("/oauth/check_token")
                                .with(httpBasic("resourceserver", "resourceserversecret"))
                                .queryParam("token", map.get("access_token"))
                )
                .andDo(print())
                .andExpect(jsonPath("$.user_name").value("user"))
                .andExpect(jsonPath("$.client_id").value("client"))
                .andExpect(status().isOk());
    }
}
