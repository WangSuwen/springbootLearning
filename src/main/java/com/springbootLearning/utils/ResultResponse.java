package com.springbootLearning.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse<T> {
    private int code;
    private T data;
    private String msg;


    public static final int SYSTEM_ERROR = 5000;
    public static final String SYSTEM_ERROR_MSG = "系统异常";
    public static final int PARAMS_ERROR = 1000;
    public static final String PARAMS_ERROR_MSG = "参数错误";


    // 构造函数
    /* private ResultResponse(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    } */

    // 静态方法用于快速构造成功响应
    public static <T> ResultResponse<T> success(T data) {
        return new ResultResponse<>(ResultEnum.SUCCESS.valueOf(), data, ResultEnum.SUCCESS.getMsg());
    }

    public static <T> ResultResponse<T> failed(String msg) {
        return new ResultResponse<>(SYSTEM_ERROR, null, msg);
    }

    public static <T> ResultResponse<T> failed(String msg, String customMsg) {
        return new ResultResponse<>(SYSTEM_ERROR, null, msg + ": " + customMsg);
    }

    public static <T> ResultResponse<T> failed(int code, String msg) {
        return new ResultResponse<>(code, null, msg);
    }

    // 静态方法用于快速构造失败响应
    public static <T> ResultResponse<T> failed(int code, String msg, String customMsg) {
        return new ResultResponse<>(code, null, customMsg);
    }
}
