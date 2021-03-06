package com.foxandgrapes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foxandgrapes.mapper.GroupMemberMapper;
import com.foxandgrapes.pojo.Group;
import com.foxandgrapes.pojo.GroupMember;
import com.foxandgrapes.pojo.Message;
import com.foxandgrapes.service.IGroupMemberService;
import com.foxandgrapes.service.IGroupService;
import com.foxandgrapes.utils.MessageUtils;
import com.foxandgrapes.vo.RespBean;
import com.foxandgrapes.ws.ChatEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
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
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember> implements IGroupMemberService {

    @Autowired
    private IGroupService groupService;
    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Override
    public RespBean joinGroup(Group group, HttpServletRequest request) {
        // 登录验证
        String userName = (String) request.getSession().getAttribute("user");
        if (userName == null) return RespBean.error("非法访问，请登录！");

        // 参数验证
        if (group == null) return RespBean.error("加入的群聊信息不能为空！");
        if (group.getName() == null || group.getName().length() < 2 || group.getName().length() > 10) {
            return RespBean.error("群聊名称不合规！");
        }
        if (group.getPassword() == null || group.getPassword().length() != 32) {
            return RespBean.error("密码不合规！");
        }

        // 获取群聊
        Group joinGroup = groupService.getGroupByName(group.getName());
        if (joinGroup == null) return RespBean.error("该群聊不存在!");

        // 判断是否已在此群聊中
        if (inGroup(group.getName(), userName)) return RespBean.error("您已在此群聊中，不能重新加入！");

        // 判断是否密码正确
        if (!group.getPassword().equals(joinGroup.getPassword())) return RespBean.error("密码不正确！");

        // 加入群聊
        if (!joinGroup(group.getName(), userName)) return RespBean.error("加入群聊失败！");

        // 添加该用户的群聊
        List<String> allGroups = ChatEndpoint.getAllGroups().get(userName);
        if (allGroups == null) {
            allGroups = new ArrayList<>();
            ChatEndpoint.getAllGroups().put(userName, allGroups);
        }
        allGroups.add(group.getName());

        // 该群聊通知在线群友并添加该在线用户
        List<String> groupOnlineUsers =  ChatEndpoint.getGroupOnlineUsers().get(group.getName());
        if (groupOnlineUsers == null) {
            groupOnlineUsers = new ArrayList<>();
            ChatEndpoint.getGroupOnlineUsers().put(group.getName(), groupOnlineUsers);
        } else {
            // 通知该群聊正在线的其他用户
            Message message = new Message();
            message.setType(14);
            message.setFromName(userName);
            message.setToName(group.getName());
            for (String user : groupOnlineUsers) {
                ChatEndpoint chatEndpoint = ChatEndpoint.getOnlineUsers().get(user);
                try {
                    chatEndpoint.getSession().getBasicRemote().sendText(MessageUtils.getMessage(message));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        groupOnlineUsers.add(userName);

        return RespBean.success("加入群聊成功！", null);
    }

    @Override
    public void getGroups(String userName) {

        // 获取群聊信息
        List<String> groupList = groupMemberMapper.getGroups(userName);
        // 保存群聊信息
        ChatEndpoint.getAllGroups().put(userName, groupList);
    }

    // 判断是否在群聊中
    @Override
    public boolean inGroup(String groupName, String memberName) {
        Integer ret = groupMemberMapper.inGroup(groupName, memberName);
        return ret != null && ret == 1;
    }

    // 加入群聊
    @Override
    public boolean joinGroup(String groupName, String memberName) {
        // 群成员信息
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupName(groupName);
        groupMember.setMemberName(memberName);

        Integer ret = groupMemberMapper.insert(groupMember);
        return ret != null && ret == 1;
    }

    @Override
    public RespBean delete(String groupName, HttpServletRequest request) {
        // 登录验证
        String userName = (String) request.getSession().getAttribute("user");
        if (userName == null) return RespBean.error("非法访问，请登录！");

        // 参数验证
        if (groupName == null) return RespBean.error("群聊名不能为空！");

        // 判断是否是群友
        if (!inGroup(groupName, userName)) return RespBean.error("您不在该群聊中，不能删除！");

        // 删除群聊
        groupMemberMapper.deleteGroup(groupName, userName);

        // 自己删除群聊的信息
        List<String> allGroups = ChatEndpoint.getAllGroups().get(userName);
        allGroups.remove(groupName);

        // 通知群友自己离群的信息（需在线）
        List<String> allUsers = ChatEndpoint.getGroupOnlineUsers().get(groupName);
        Message message = new Message();
        message.setType(15);
        message.setFromName(userName);
        message.setToName(groupName);
        for (String user : allUsers) {
            if (userName.equals(user)) continue;

            ChatEndpoint chatEndpoint = ChatEndpoint.getOnlineUsers().get(user);
            try {
                chatEndpoint.getSession().getBasicRemote().sendText(MessageUtils.getMessage(message));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        allUsers.remove(userName);
        if (allUsers.size() == 0) ChatEndpoint.getGroupOnlineUsers().remove(groupName);

        return RespBean.success("群聊删除成功！", null);
    }
}
