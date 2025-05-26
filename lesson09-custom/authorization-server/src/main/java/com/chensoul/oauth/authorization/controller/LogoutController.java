package com.chensoul.oauth.authorization.controller;

import com.chensoul.oauth.common.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class LogoutController {
	@Nullable
	private final RedisTokenStore redisTokenStore;

	/**
	 * 安全退出
	 *
	 * @param access_token
	 * @param authorization
	 * @return
	 */
	@GetMapping("/logout")
	public RestResponse logout(String access_token, final String authorization) {
		if (StringUtils.isBlank(access_token)) {
			access_token = authorization;
		}
		if (StringUtils.isBlank(access_token)) {
			return RestResponse.ok();
		}
		if (access_token.toLowerCase().contains("bearer ".toLowerCase())) {
			access_token = access_token.toLowerCase().replace("bearer ", "");
		}
		final OAuth2AccessToken oAuth2AccessToken = this.redisTokenStore.readAccessToken(access_token);
		if (oAuth2AccessToken != null && this.redisTokenStore != null) {
			this.redisTokenStore.removeAccessToken(oAuth2AccessToken);
			final OAuth2RefreshToken refreshToken = oAuth2AccessToken.getRefreshToken();
			this.redisTokenStore.removeRefreshToken(refreshToken);
		}
		return RestResponse.ok();
	}

}
