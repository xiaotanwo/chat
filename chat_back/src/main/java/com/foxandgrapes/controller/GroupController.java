package com.foxandgrapes.controller;


import com.foxandgrapes.pojo.Group;
import com.foxandgrapes.service.IGroupService;
import com.foxandgrapes.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private IGroupService groupService;

    @PostMapping("/newGroup")
    public RespBean newGroup(@RequestBody Group group, HttpServletRequest request) {
        return groupService.newGroup(group, request);
    }
}
