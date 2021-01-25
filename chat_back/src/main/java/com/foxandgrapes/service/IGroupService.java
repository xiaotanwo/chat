package com.foxandgrapes.service;

import com.foxandgrapes.pojo.Group;
import com.baomidou.mybatisplus.extension.service.IService;
import com.foxandgrapes.vo.RespBean;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tsk
 * @since 2021-01-22
 */
public interface IGroupService extends IService<Group> {

    /**
     * 新建群聊
     * @param group
     * @param request
     * @return
     */
    RespBean newGroup(Group group, HttpServletRequest request);

    /**
     * 查询是否存在群聊
     * @param name
     * @return
     */
    boolean existGroup(String name);

    /**
     * 通过群聊名获取群聊
     * @param name
     * @return
     */
    Group getGroupByName(String name);

}
