package com.chensoul.oauth;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * Needs the following to be running:
 * - Authorization Server
 */
public class AuthServerLiveTest {

  private static final String USERNAME = "user";
  private static final String PASSWORD = "pass";

  private static final String CLIENT_ID = "client";
  private static final String CLIENT_SECRET = "secret";

  private static final String AUTH_SERVER_BASE_URL = "http://localhost:8083";
  private static final String CLIENT_BASE_URL = "http://localhost:8082";

  private static final String REDIRECT_URL = CLIENT_BASE_URL + "/lsso-client/login/oauth2/code/custom";
  private static final String AUTHORIZE_URL =
          AUTH_SERVER_BASE_URL + "/oauth/authorize?response_type=code&client_id=client&scope=read&redirect_uri=" + REDIRECT_URL;
  private static final String TOKEN_URL = AUTH_SERVER_BASE_URL + "/oauth/token";

  @Test
  public void givenAuthorizationCodeGrant_whenObtainAccessToken_thenSuccess() {
    String accessToken = obtainAccessToken();

    assertThat(accessToken).isNotBlank();
  }

  @Test
  public void whenServiceStarts_thenKeysEndpointIsAvailable() {
    final String keysUrl = AUTH_SERVER_BASE_URL + "/endpoint/jwks.json";

    Response response = RestAssured.given()
            .redirects()
            .follow(false)
            .get(keysUrl);

    assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatusCode());
    System.out.println(response.asString());
    assertThat(response.jsonPath()
            .getMap("$.")).containsKeys("keys");
  }

  private String obtainAccessToken() {
    // obtain authentication url with custom codes
    Response response = RestAssured.given()
            .redirects()
            .follow(false)
            .get(AUTHORIZE_URL);
    String authSessionId = response.getCookie("JSESSIONID");
    String kcPostAuthenticationUrl = AUTH_SERVER_BASE_URL + "/login";

    // open login form
    response = RestAssured.given()
            .cookie("JSESSIONID", authSessionId)
            .get(kcPostAuthenticationUrl);

    String csrf = response.asString()
            .split("value=\"")[1].split("\"")[0];

    // obtain authentication code and state
    response = RestAssured.given()
            .redirects()
            .follow(false)
            .cookie("JSESSIONID", authSessionId)
            .formParams("username", USERNAME, "password", PASSWORD, "_csrf", csrf)
            .post(kcPostAuthenticationUrl);
    assertThat(HttpStatus.FOUND.value()).isEqualTo(response.getStatusCode());

    String location = URLDecoder.decode(response.getHeader(HttpHeaders.LOCATION));
    authSessionId = response.getCookie("JSESSIONID");

    // redirect to client url
    response = RestAssured.given()
            .redirects()
            .follow(false)
            .cookie("JSESSIONID", authSessionId)
            .get(location);

    System.out.println(response.asString());
    // extract authorization code
    location = response.getHeader(HttpHeaders.LOCATION);
    String code = location.split("code=")[1].split("&")[0];

    // get access token
    Charset charset = StandardCharsets.ISO_8859_1;
    String basicAuth = new String(Base64.getEncoder()
            .encode((CLIENT_ID + ":" + CLIENT_SECRET).getBytes(charset)), charset);

    Map<String, String> params = new HashMap<String, String>();
    params.put("grant_type", "authorization_code");
    params.put("code", code);
    params.put("redirect_uri", REDIRECT_URL);
    response = RestAssured.given()
            .header("Authorization", "Basic " + basicAuth)
            .queryParams(params)
            .post(TOKEN_URL);
    return response.jsonPath()
            .getString("access_token");
  }

}
