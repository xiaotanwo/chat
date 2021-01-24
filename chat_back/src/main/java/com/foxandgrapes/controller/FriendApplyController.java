package com.foxandgrapes.controller;


import com.foxandgrapes.service.IFriendApplyService;
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
@RequestMapping("/friendApply")
public class FriendApplyController {

    @Autowired
    private IFriendApplyService friendApplyService;

    @RequestMapping("/add/{applyName}/{msg}")
    public RespBean add(@PathVariable("applyName") String applyName,
                        @PathVariable("msg") String msg,
                        HttpServletRequest request) {
        return friendApplyService.add(applyName, msg, request);
    }

    @RequestMapping("/search")
    public RespBean search(HttpServletRequest request) {
        return friendApplyService.search(request);
    }

    @RequestMapping("/agree/{name}")
    public RespBean agree(@PathVariable("name") String name, HttpServletRequest request) {
        return friendApplyService.agree(name, request);
    }

    @RequestMapping("/refuse/{name}")
    public RespBean refuse(@PathVariable("name") String name, HttpServletRequest request) {
        return friendApplyService.refuse(name, request);
    }
}
