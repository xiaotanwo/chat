package com.foxandgrapes.controller;


import com.foxandgrapes.pojo.Group;
import com.foxandgrapes.service.IGroupMemberService;
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
 * @since 2021-01-22
 */
@RestController
@RequestMapping("/groupMember")
public class GroupMemberController {

    @Autowired
    private IGroupMemberService groupMemberService;

    @PostMapping("/joinGroup")
    public RespBean joinGroup(@RequestBody Group group, HttpServletRequest request) {
        return groupMemberService.joinGroup(group, request);
    }

    @RequestMapping("/getGroups")
    public RespBean getGroups(HttpServletRequest request) {
        return groupMemberService.getGroups(request);
    }
}
