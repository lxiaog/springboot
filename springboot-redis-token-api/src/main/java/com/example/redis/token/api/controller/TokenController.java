package com.example.redis.token.api.controller;

import com.example.redis.token.api.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Work
 */
@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/token", produces = "application/json;charset=UTF-8")
    public Object getToken() {
        return tokenService.getToken();
    }
}
