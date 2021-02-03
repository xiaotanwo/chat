package com.foxandgrapes.controller;


import com.foxandgrapes.service.IFriendService;
import com.foxandgrapes.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tsk
 * @since 2021-01-22
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private IFriendService friendService;

    @RequestMapping("/delete/{friendName}")
    public RespBean delete(@PathVariable("friendName") String friendName, HttpServletRequest request) {
        return friendService.delete(friendName, request);
    }
}