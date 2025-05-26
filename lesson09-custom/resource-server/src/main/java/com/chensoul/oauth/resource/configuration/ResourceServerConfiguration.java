package com.chensoul.oauth.resource.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@EnableResourceServer
@Configuration
@RequiredArgsConstructor
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	private final ResourceServerTokenServices tokenServices;
	private final AccessDeniedHandler accessDeniedHandler;
	private final AuthenticationEntryPoint authenticationEntryPoint;

	@Override
	public void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().authenticated();
	}

	@Override
	public void configure(final ResourceServerSecurityConfigurer resources) {
		resources
			//无状态化,每次访问都需认证
			.stateless(true)
			//自定义Token异常信息,用于token校验失败返回信息
			.authenticationEntryPoint(authenticationEntryPoint)
			//授权异常处理
			.accessDeniedHandler(accessDeniedHandler);

		if (tokenServices != null) {
			resources.tokenServices(tokenServices);
		}
	}
}
