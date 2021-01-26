package com.foxandgrapes.service;

import com.foxandgrapes.pojo.Friend;
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
public interface IFriendService extends IService<Friend> {

    /**
     * 获取所有好友
     * @param request
     * @return
     */
    RespBean getFriends(HttpServletRequest request);

    /**
     * 删除好友
     * @param friendName
     * @param request
     * @return
     */
    RespBean delete(String friendName, HttpServletRequest request);

    /**
     * 插入好友关系
     * @param name
     * @param friendName
     * @return
     */
    boolean insertFriend(String name, String friendName);

    /**
     * 查看是否为好友
     * @param name
     * @return
     */
    boolean isFriend(String name, String friendName);
}
