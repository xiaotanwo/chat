package com.foxandgrapes.service;

import com.foxandgrapes.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.foxandgrapes.vo.RespBean;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tsk
 * @since 2021-01-19
 */
public interface IUserService extends IService<User> {

    /**
     * 登录
     * @param user
     * @param request
     * @return
     */
    RespBean login(User user, HttpServletRequest request);

    /**
     * 注册
     * @param user
     * @param request
     * @return
     */
    RespBean register(User user, HttpServletRequest request);

    /**
     * 注销
     * @return
     */
    RespBean logout(HttpServletRequest request);
}
