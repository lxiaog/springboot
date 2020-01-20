package com.example.redis.token.api.service.impl;

import com.example.redis.token.api.common.utils.ConstantUtil;
import com.example.redis.token.api.common.rest.RestResult;
import com.example.redis.token.api.config.redis.RedisCache;
import com.example.redis.token.api.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisCache redisCache;

    @Override
    public String createToken() {
        String token = UUID.randomUUID().toString().trim().replaceAll("-", "");
        boolean is = redisCache.set(ConstantUtil.Redis.TOKEN_PREFIX.concat(token), token, ConstantUtil.Redis.TOKEN_EXPIRE_TIME_SECOND);
        if (is) {
            return token;
        }
        return "";
    }

    @Override
    public RestResult getToken() {
        String token = createToken();
        if (StringUtils.isEmpty(token)) {
            return RestResult.fail().setMsg("获取token失败");
        }
        return RestResult.ok().setToken(token).setMsg("获取token成功");

    }
}
