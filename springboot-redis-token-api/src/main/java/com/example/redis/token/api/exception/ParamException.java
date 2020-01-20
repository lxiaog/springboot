package com.example.redis.token.api.exception;

import com.example.redis.token.api.common.rest.ResponseCode;

/**
 * 参数异常
 */
public class ParamException extends RuntimeException {

    private String code;
    private String msg;


    public ParamException() {
        super();
    }

    public ParamException(String message) {
        super(message);
    }

    public ParamException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ParamException(ResponseCode code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(Throwable cause) {
        super(cause);
    }

    protected ParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
