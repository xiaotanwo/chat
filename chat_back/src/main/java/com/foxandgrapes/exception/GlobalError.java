package com.foxandgrapes.exception;

import com.foxandgrapes.vo.RespBean;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常错误类
 */
@RestController
public class GlobalError implements ErrorController {

    @RequestMapping("/error")
    public RespBean handleError(HttpServletRequest request) {

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        return RespBean.error("出现错误： " + statusCode);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}