

package com.chensoul.oauth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SecureController {
  @GetMapping("/secured/{id}")
  public String secured(@PathVariable String id) {
    return "secured " + id;
  }
}
