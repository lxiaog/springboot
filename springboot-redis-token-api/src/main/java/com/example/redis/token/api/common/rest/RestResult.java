package com.example.redis.token.api.common.rest;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RestResult {
    private String code;
    private String msg;
    private Object data;
    private String token;
    private Long timestamp;


    public static RestResult restResult() {
        return new RestResult().setTimestamp(System.currentTimeMillis());
    }

    public static RestResult ok() {
        return restResult()
                .setCode(ResponseCode.OK.getCode())
                .setMsg(ResponseCode.OK.getMsg());
    }

    public static RestResult ok(Object data) {
        return ok().setData(data);
    }


    public static RestResult fail() {
        return restResult()
                .setCode(ResponseCode.FAIL.getCode())
                .setMsg(ResponseCode.FAIL.getMsg());
    }


    public static RestResult error() {
        return restResult().setCode(ResponseCode.ERROR.getCode()).setMsg(ResponseCode.ERROR.getMsg());
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }
}
