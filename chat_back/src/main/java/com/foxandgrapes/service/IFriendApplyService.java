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
     * @param friendApply
     * @param request
     * @return
     */
    RespBean add(FriendApply friendApply, HttpServletRequest request);
}
