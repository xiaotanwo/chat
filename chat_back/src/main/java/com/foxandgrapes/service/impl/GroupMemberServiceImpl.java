package com.foxandgrapes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foxandgrapes.mapper.GroupMemberMapper;
import com.foxandgrapes.pojo.Group;
import com.foxandgrapes.pojo.GroupMember;
import com.foxandgrapes.service.IGroupMemberService;
import com.foxandgrapes.service.IGroupService;
import com.foxandgrapes.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        return RespBean.success("加入群聊成功！", null);
    }

    @Override
    public RespBean getGroups(HttpServletRequest request) {
        // 登录验证
        String userName = (String) request.getSession().getAttribute("user");
        if (userName == null) return RespBean.error("非法访问，请登录！");

        return RespBean.success("获取群聊信息成功！", groupMemberMapper.getGroups(userName));
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
}
