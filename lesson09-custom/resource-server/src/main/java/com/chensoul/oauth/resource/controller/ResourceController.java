package com.chensoul.oauth.resource.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ResourceController {

    @GetMapping("/public")
    public String common() {
        return "Public Resource!";
    }

    @GetMapping("/resource")
    public String resource() {
        return "Secured Web Resource!";
    }
}
