package com.foxandgrapes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foxandgrapes.mapper.FriendApplyMapper;
import com.foxandgrapes.pojo.FriendApply;
import com.foxandgrapes.pojo.User;
import com.foxandgrapes.service.IFriendApplyService;
import com.foxandgrapes.service.IFriendService;
import com.foxandgrapes.service.IUserService;
import com.foxandgrapes.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class FriendApplyServiceImpl extends ServiceImpl<FriendApplyMapper, FriendApply> implements IFriendApplyService {

    @Autowired
    private IUserService userService;
    @Autowired
    private IFriendService friendService;
    @Autowired
    private FriendApplyMapper friendApplyMapper;

    @Override
    public RespBean add(FriendApply friendApply, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) return RespBean.error("非法访问！请登录！");

        if (friendApply == null) return RespBean.error("好友申请不能为空！");
        // 申请的好友名称
        String applyName = friendApply.getApplyName();
        if (applyName == null) return RespBean.error("好友名称不能为空！");
        if (applyName.equals(user.getName())) return RespBean.error("不能添加自己为好友！");

        // 验证好友是否存在
        User friend = userService.getUserByName(applyName);
        if (friend == null) return RespBean.error("该好友不存在！");

        // 验证是否已是好友
        if (friendService.isFriend(user.getName(), applyName)) {
            return RespBean.error("已是好友！");
        }

        // 验证是否已经申请过
        QueryWrapper<FriendApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", user.getName()).eq("apply_name", applyName);
        List<FriendApply> friendApplyList = friendApplyMapper.selectList(queryWrapper);
        if (friendApplyList != null && friendApplyList.size() > 0) {
            return RespBean.error("不能对同一好友进行重复申请！");
        }

        // 防止前端传来的参数有误
        friendApply.setName(user.getName());
        friendApply.setStatus(null);

        // 进行好友申请
        int ret = friendApplyMapper.insert(friendApply);
        if (ret != 1) {
            return RespBean.error("好友申请失败！");
        }

        return RespBean.success("好友申请成功！", null);
    }

    @Override
    public RespBean search(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) return RespBean.error("非法访问！请登录！");

        // 只查询未作回应的好友申请
        QueryWrapper<FriendApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("apply_name", user.getName()).eq("status", 0);
        List<FriendApply> friendApplyList = friendApplyMapper.selectList(queryWrapper);

        return RespBean.success("查询好友申请成功！", friendApplyList);
    }

    @Transactional
    @Override
    public RespBean agree(String name, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) return RespBean.error("非法访问！请登录！");

        if (name == null) return RespBean.error("名称不能为空！");

        // 验证是否存在
        QueryWrapper<FriendApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name).eq("apply_name", user.getName()).eq("status", 0);
        List<FriendApply> friendApplyList = friendApplyMapper.selectList(queryWrapper);
        if (friendApplyList == null || friendApplyList.isEmpty()) {
            return RespBean.error("该好友申请不存在！");
        }

        // 同意
        FriendApply respondFriendApply = friendApplyList.get(0);
        respondFriendApply.setStatus(1);
        int ret = friendApplyMapper.updateById(respondFriendApply);
        if (ret != 1) {
            return RespBean.error("好友申请同意失败！");
        }

        // 验证是否已是好友
        if (friendService.isFriend(user.getName(), name)) {
            return RespBean.success("已是好友！", null);
        }

        friendService.insertFriend(user.getName(), name);
        friendService.insertFriend(name, user.getName());

        return RespBean.success("好友申请同意成功！", null);
    }

    @Override
    public RespBean refuse(String name, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) return RespBean.error("非法访问！请登录！");

        if (name == null) return RespBean.error("名称不能为空！");

        // 验证是否存在
        QueryWrapper<FriendApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name).eq("apply_name", user.getName());
        List<FriendApply> friendApplyList = friendApplyMapper.selectList(queryWrapper);
        if (friendApplyList == null || friendApplyList.isEmpty()) {
            return RespBean.error("该好友申请不存在！");
        }

        // 拒绝
        FriendApply respondFriendApply = friendApplyList.get(0);
        respondFriendApply.setStatus(2);

        int ret = friendApplyMapper.updateById(respondFriendApply);
        if (ret != 1) {
            return RespBean.error("好友申请拒绝失败！");
        }

        return RespBean.success("好友申请拒绝成功！", null);
    }
}
