package com.foxandgrapes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foxandgrapes.mapper.FriendMapper;
import com.foxandgrapes.pojo.Friend;
import com.foxandgrapes.service.IFriendService;
import com.foxandgrapes.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tsk
 * @since 2021-01-22
 */
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements IFriendService {

    @Autowired
    private FriendMapper friendMapper;

    @Override
    public RespBean getFriends(HttpServletRequest request) {
        // 登录验证
        String userName = (String) request.getSession().getAttribute("user");
        if (userName == null) return RespBean.error("非法访问，请登录！");

        // 查询好友列表
        return RespBean.success("查询好友成功！", friendMapper.getFriendList(userName));
    }

    @Transactional
    @Override
    public RespBean delete(String friendName, HttpServletRequest request) {
        // 登录验证
        String userName = (String) request.getSession().getAttribute("user");
        if (userName == null) return RespBean.error("非法访问，请登录！");

        // 参数验证
        if (friendName == null) return RespBean.error("好友名不能为空！");

        // 判断是否是朋友
        if (!isFriend(userName, friendName)) return RespBean.error("该用户不是您好友！");

        // 双向删除好友关系，事务
        deleteFriend(userName, friendName);
        deleteFriend(friendName, userName);

        return RespBean.success("好友删除成功！", null);
    }

    // 删除好友
    private Boolean deleteFriend(String name, String friend) {
        Integer ret = friendMapper.deleteFriend(name, friend);
        return ret != null && ret == 1;
    }

    // 判断是否是好友
    @Override
    public boolean isFriend(String name, String friend) {
        Integer ret = friendMapper.isFriend(name, friend);
        return ret != null && ret == 1;
    }

    // 添加好友
    @Override
    public boolean insertFriend(String name, String friendName) {
        // 好友信息
        Friend friend = new Friend();
        friend.setName(name);
        friend.setFriend(friendName);
        Integer ret = friendMapper.insert(friend);
        return ret != null && ret == 1;
    }

}
