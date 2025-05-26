package com.chensoul.oauth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ResourceServerApplicationTests {

  @Autowired
  private MockMvc mvc;

  @Test
  public void homePageAvailable() throws Exception {
    this.mvc.perform(get("/").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
  }

  @Test
  public void flightsSecuredByDefault() throws Exception {
    this.mvc.perform(get("/secured/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    this.mvc.perform(get("/secured/2").accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());

  }

}
