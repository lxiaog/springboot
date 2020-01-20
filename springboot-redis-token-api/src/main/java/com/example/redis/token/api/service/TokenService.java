package com.example.redis.token.api.service;

import com.example.redis.token.api.common.rest.RestResult;

public interface TokenService {

    String createToken();


    RestResult getToken();



}
