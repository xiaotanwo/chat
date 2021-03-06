package com.foxandgrapes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foxandgrapes.mapper.GroupMapper;
import com.foxandgrapes.pojo.Group;
import com.foxandgrapes.service.IGroupMemberService;
import com.foxandgrapes.service.IGroupService;
import com.foxandgrapes.vo.RespBean;
import com.foxandgrapes.ws.ChatEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {

    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private IGroupMemberService groupMemberService;

    @Transactional
    @Override
    public RespBean newGroup(Group group, HttpServletRequest request) {
        // 登录验证
        String userName = (String) request.getSession().getAttribute("user");
        if (userName == null) return RespBean.error("非法访问，请登录！");

        // 参数验证
        if (group == null) return RespBean.error("新建群聊信息不能为空！");
        if (group.getName() == null || group.getName().length() < 2 || group.getName().length() > 10) {
            return RespBean.error("群聊名称不合规！");
        }
        if (group.getPassword() == null || group.getPassword().length() != 32) {
            return RespBean.error("密码不合规！");
        }

        // 验证是否已存在该群名
        if (existGroup(group.getName())) return RespBean.error("该群聊已存在，请更改群聊名称！");

        // 事务
        groupMapper.insert(group);
        groupMemberService.joinGroup(group.getName(), userName);

        // 保存新建群聊的相关信息
        List<String> allGroups = ChatEndpoint.getAllGroups().get(userName);
        if (allGroups == null) {
            allGroups = new ArrayList<>();
            ChatEndpoint.getAllGroups().put(userName, allGroups);
        }
        allGroups.add(group.getName());
        List<String> groupOnlineUsers = ChatEndpoint.getGroupOnlineUsers().get(group.getName());
        if (groupOnlineUsers == null) {
            groupOnlineUsers = new ArrayList<>();
            ChatEndpoint.getGroupOnlineUsers().put(group.getName(), groupOnlineUsers);
        }
        groupOnlineUsers.add(userName);

        return RespBean.success("新建群聊成功！", null);
    }

    // 判断群聊是否已存在
    @Override
    public boolean existGroup(String name) {
        Group group = getGroupByName(name);
        return group != null;
    }

    // 通过群聊名称获取群聊
    @Override
    public Group getGroupByName(String name) {
        return groupMapper.getGroupByName(name);
    }

}
