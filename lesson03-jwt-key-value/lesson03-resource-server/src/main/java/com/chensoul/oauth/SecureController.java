

package com.chensoul.oauth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SecureController {

  @GetMapping("/secured/{id}")
  public String secured(@PathVariable String id) {
    return SecurityContextHolder.getContext().getAuthentication().getName() + ", secured " + id;
  }
}
