package com.chensoul.oauth.authorization.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * TODO Comment
 *
 * @author <a href="mailto:chensoul.eth@gmail.com">Chensoul</a>
 * @since TODO
 */
@RequiredArgsConstructor
public class TokenStoreLogoutHandler implements LogoutHandler {
	private final TokenStore tokenStore;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		if (tokenStore != null) {
			String token = request.getHeader(HttpHeaders.AUTHORIZATION)
				.replace(OAuth2AccessToken.BEARER_TYPE, "").trim();

			OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
			if (accessToken != null) {
				if (accessToken.getRefreshToken() != null) {
					tokenStore.removeRefreshToken(accessToken.getRefreshToken());
				}
				tokenStore.removeAccessToken(accessToken);
			}
		}
	}
}
