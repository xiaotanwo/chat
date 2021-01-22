package com.foxandgrapes.controller;


import com.foxandgrapes.pojo.FriendApply;
import com.foxandgrapes.service.IFriendApplyService;
import com.foxandgrapes.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/friendApply")
public class FriendApplyController {

    @Autowired
    private IFriendApplyService friendApplyService;

    @PostMapping("/add")
    public RespBean add(FriendApply friendApply, HttpServletRequest request) {
        return friendApplyService.add(friendApply, request);
    }
}
