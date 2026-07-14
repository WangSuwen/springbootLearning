package com.springbootLearning.handler;

import com.alibaba.fastjson2.JSON;
import com.springbootLearning.utils.ResultResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;  
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.springbootLearning.utils.ResultEnum;
import org.springframework.beans.factory.annotation.Value;




import java.util.List;

/**
 * @Description: 统一异常处理类
 * @Auther: Zhangjiashun
 * @create 2019/10/18 11:51
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    /**
     * 处理特定异常
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    public ResultResponse<?> error(BadSqlGrammarException e) {
        this.outErrorLog(e);
        return ResultResponse.failed("系统异常", "错误的SQL语句");
    }

    @ExceptionHandler(BindException.class)
    public ResultResponse<?> error(BindException e) {
        ObjectError objectError = e.getAllErrors().get(0);
        return ResultResponse.failed("参数错误", objectError.getDefaultMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultResponse<?> error(HttpMessageNotReadableException e) {
        this.outErrorLog(e);
        return ResultResponse.failed("Json解析异常");
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResultResponse<?> error(HttpRequestMethodNotSupportedException e) {

        return ResultResponse.failed(ResultEnum.METHOD_NOT_ALLOWED.valueOf(), "不支持的" + e.getMethod() + "请求方式");
    }


    @ExceptionHandler(value = Exception.class)
    public ResultResponse<?> error(Exception e) {
        this.outErrorLog(e);

       if(StringUtils.isNotBlank(e.getMessage())){
           return ResultResponse.failed(e.getMessage());
       }else{
           return ResultResponse.failed(ResultEnum.FAILED.getMsg());
       }

    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultResponse<?> valid(MethodArgumentNotValidException e) {
        StringBuffer buffer = new StringBuffer();
        BindingResult result  = e.getBindingResult();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(p ->{
                FieldError fieldError = (FieldError) p;
                buffer.append(fieldError.getDefaultMessage()).append(",");
            });
        }
        return ResultResponse.failed(ResultEnum.FAILED.valueOf(), buffer.toString());
    }


    /**
     * 输出错误日志
     *
     * @param e 错误
     */
    private void outErrorLog(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        /* if (StringUtils.equals(activeProfile, "dev")) {
            log.error(ExceptionUtils.getStackTrace(e));
            // log.error(ExceptionUtils.getMessage(e));
        } else if (StringUtils.equals(activeProfile, "prod")) {
            // 正式环境日志写入异常信息 正式上线之前写入堆栈
            log.error(ExceptionUtils.getStackTrace(e));
        } else {
            log.error(ExceptionUtils.getStackTrace(e));
        } */
    }
}
