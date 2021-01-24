package com.foxandgrapes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foxandgrapes.mapper.FriendMapper;
import com.foxandgrapes.pojo.Friend;
import com.foxandgrapes.pojo.User;
import com.foxandgrapes.service.IFriendService;
import com.foxandgrapes.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) return RespBean.error("非法访问，请登录！");

        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", user.getName());
        List<Friend> friendList = friendMapper.selectList(queryWrapper);

        return RespBean.success("查询好友成功！", friendList);
    }

    @Override
    public int insertFriend(String name, String friendName) {
        Friend friend = new Friend();
        friend.setName(name);
        friend.setFriend(friendName);
        return friendMapper.insert(friend);
    }

    @Override
    public boolean isFriend(String name, String friendName) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name).eq("friend", friendName);
        List<Friend> friendList = friendMapper.selectList(queryWrapper);
        if (friendList == null || friendList.size() == 0) {
            return false;
        }
        return true;
    }

}
