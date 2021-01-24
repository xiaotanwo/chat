package com.foxandgrapes.service;

import com.foxandgrapes.pojo.FriendApply;
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
public interface IFriendApplyService extends IService<FriendApply> {

    /**
     * 添加好友
     * @param applyName
     * @param msg
     * @param request
     * @return
     */
    RespBean add(String applyName, String msg, HttpServletRequest request);

    /**
     * 查看好友申请
     * @param request
     * @return
     */
    RespBean search(HttpServletRequest request);

    /**
     * 同意
     * @param name
     * @param request
     * @return
     */
    RespBean agree(String name, HttpServletRequest request);

    /**
     * 拒绝
     * @param name
     * @param request
     * @return
     */
    RespBean refuse(String name, HttpServletRequest request);
}
