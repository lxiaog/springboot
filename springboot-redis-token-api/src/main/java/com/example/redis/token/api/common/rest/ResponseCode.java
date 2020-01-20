package com.example.redis.token.api.common.rest;

/**
 * 响应码
 */

public enum ResponseCode {
    //交易响应码
    OK("0", "交易成功"),
    FAIL("1", "交易失败"),
    ERROR("2", "交易错误"),
    /**
     * 参数错误码
     */
    PARAM_IS_NULL("1000", "参数为空"),
    PARAM_INVALID("1001", "参数无效"),
    /**
     * 操作错误码
     */
    OPERATION_NON_REPEAT("1100", "请勿重复操作"),
    /**
     * 订单错误码
     */
    ORDER_IS_NULL("1200","该订单不存在"),
    ORDER_IS_EXIST("1201","该订单已存在")
;
    private String code;
    private String msg;

    ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
