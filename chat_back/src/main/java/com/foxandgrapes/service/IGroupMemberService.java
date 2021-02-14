package com.foxandgrapes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.foxandgrapes.pojo.Group;
import com.foxandgrapes.pojo.GroupMember;
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
public interface IGroupMemberService extends IService<GroupMember> {

    /**
     * 加入群聊
     * @param group
     * @param request
     * @return
     */
    RespBean joinGroup(Group group, HttpServletRequest request);

    /**
     * 获取用户所有的群聊
     * @param userName
     * @return
     */
    void getGroups(String userName);

    /**
     * 判断是否在群聊中
     * @param groupName
     * @param memberName
     * @return
     */
    boolean inGroup(String groupName, String memberName);

    /**
     * 加入群聊
     * @param groupName
     * @param memberName
     * @return
     */
    boolean joinGroup(String groupName, String memberName);

    /**
     * 删除群聊
     * @param groupName
     * @param request
     * @return
     */
    RespBean delete(String groupName, HttpServletRequest request);
}
