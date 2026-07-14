package com.springbootLearning.utils;

public enum ResultEnum {
    SUCCESS(200, "请求成功"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    FAILED(5000, "系统错误"),
    PARAMS_ERROR(1000, "参数错误"),
    ;


    private final int code;
    private final String msg;

    @Override
    public String toString() {
        return "ResultEnum{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public int valueOf () {
        return code;
    }

    public String getMsg () {
        return msg;
    }

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
