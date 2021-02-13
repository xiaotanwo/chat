package com.foxandgrapes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foxandgrapes.mapper.FriendMapper;
import com.foxandgrapes.pojo.Friend;
import com.foxandgrapes.pojo.Message;
import com.foxandgrapes.service.IFriendService;
import com.foxandgrapes.utils.MessageUtils;
import com.foxandgrapes.vo.RespBean;
import com.foxandgrapes.ws.ChatEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    public void getFriends(String userName) {

        // 获取好友信息
        List<String> friendList = friendMapper.getFriendList(userName);
        // 保存好友信息
        ChatEndpoint.getAllFriends().put(userName, friendList);
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

        // 自己删除好友的信息
        List<String> allFriends = ChatEndpoint.getAllFriends().get(userName);
        allFriends.remove(friendName);

        // 通知好友删除自己的信息（需在线）
        ChatEndpoint friendChatEndpoint = ChatEndpoint.getOnlineUsers().get(friendName);
        if (friendChatEndpoint != null) {
            // 好友边的删除
            allFriends = ChatEndpoint.getAllFriends().get(friendName);
            allFriends.remove(userName);

            // 双向在线好友删除
            ChatEndpoint.getOnlineFriends().get(userName).remove(friendName);
            ChatEndpoint.getOnlineFriends().get(friendName).remove(userName);

            Message message = new Message();
            // 离线通知
            message.setType(25);
            try {
                // 自己通知
                message.setFromName(friendName);
                ChatEndpoint.getOnlineUsers().get(userName).getSession().getBasicRemote().sendText(MessageUtils.getMessage(message));
                // 好友通知
                message.setFromName(userName);
                friendChatEndpoint.getSession().getBasicRemote().sendText(MessageUtils.getMessage(message));
                // 删除通知
                message.setType(28);
                friendChatEndpoint.getSession().getBasicRemote().sendText(MessageUtils.getMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
