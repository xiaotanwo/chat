package com.foxandgrapes.exception;

import com.foxandgrapes.vo.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionHadler {

    @ExceptionHandler(Exception.class)
    public RespBean exceptionHandler(Exception e) {
        return RespBean.error("服务器异常！");
    }
}
