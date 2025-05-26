package com.chensoul.oauth.authorization.controller;

import com.chensoul.oauth.common.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户中心
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserDetailsService userDetailsService;

  @GetMapping("/userinfo")
  public RestResponse<UserDetails> userinfo(final Authentication authentication) {
    final Object principal = authentication.getPrincipal();

    UserDetails userDetails = null;
    if (principal instanceof UserDetails) {
      userDetails = (UserDetails) principal;
    } else {
      final String username = principal.toString();
      userDetails = this.userDetailsService.loadUserByUsername(username);
      ((CredentialsContainer) userDetails).eraseCredentials();
    }
    return RestResponse.ok(userDetails);
  }

}
