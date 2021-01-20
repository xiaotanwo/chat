package com.foxandgrapes.controller;


import com.foxandgrapes.pojo.User;
import com.foxandgrapes.service.IUserService;
import com.foxandgrapes.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tsk
 * @since 2021-01-19
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public RespBean login(@RequestBody User user, HttpServletRequest request) {
        return userService.login(user, request);
    }

    @PostMapping("/register")
    public RespBean register(@RequestBody User user, HttpServletRequest request) {
        return userService.register(user, request);
    }
}
