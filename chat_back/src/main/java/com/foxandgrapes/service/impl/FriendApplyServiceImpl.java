package com.foxandgrapes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foxandgrapes.mapper.FriendApplyMapper;
import com.foxandgrapes.pojo.FriendApply;
import com.foxandgrapes.service.IFriendApplyService;
import com.foxandgrapes.service.IFriendService;
import com.foxandgrapes.service.IUserService;
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
public class FriendApplyServiceImpl extends ServiceImpl<FriendApplyMapper, FriendApply> implements IFriendApplyService {

    @Autowired
    private IUserService userService;
    @Autowired
    private IFriendService friendService;
    @Autowired
    private FriendApplyMapper friendApplyMapper;

    @Override
    public RespBean add(FriendApply friendApply, HttpServletRequest request) {
        // 登录验证
        String userName = (String) request.getSession().getAttribute("user");
        if (userName == null) return RespBean.error("非法访问！请登录！");

        // 参数检查
        if (friendApply == null) return RespBean.error("好友申请不能为空！");
        // 申请的好友名称
        String applyName = friendApply.getApplyName();
        if (applyName == null) return RespBean.error("好友名称不能为空！");
        if (applyName.equals(userName)) return RespBean.error("不能添加自己为好友！");

        // 验证好友是否存在
        if (!userService.isUser(applyName)) return RespBean.error("该好友不存在！");

        // 验证是否已是好友
        if (friendService.isFriend(userName, applyName)) return RespBean.error("已是好友！");

        // 验证是否已经申请过
        if (applyed(userName, applyName)) return RespBean.error("不能对同一好友进行重复申请！");

        // 好友申请
        if (!apply(userName, applyName, friendApply.getMsg())) return RespBean.error("好友申请失败！");

        return RespBean.success("好友申请成功！", null);
    }

    @Override
    public RespBean search(HttpServletRequest request) {
        // 登录验证
        String userName = (String) request.getSession().getAttribute("user");
        if (userName == null) return RespBean.error("非法访问！请登录！");

        // 查询未作回应的好友申请
        return RespBean.success("查询好友申请成功！", friendApplyMapper.getFriendApplyListWithNotRespond(userName));
    }

    @Transactional
    @Override
    public RespBean agree(String name, HttpServletRequest request) {
        // 登录验证
        String userName = (String) request.getSession().getAttribute("user");
        if (userName == null) return RespBean.error("非法访问！请登录！");

        // 参数检查
        if (name == null) return RespBean.error("名称不能为空！");

        // 验证同意的未回应的好友申请是否存在
        FriendApply friendApply = friendApplyMapper.getFriendApplyWithNotRespond(name, userName);
        if (friendApply == null) return RespBean.error("该未反应的好友申请不存在！");

        // 同意
        friendApply.setStatus(1);
        friendApplyMapper.updateById(friendApply);

        // 验证是否已是好友
        if (friendService.isFriend(userName, name)) return RespBean.success("已是好友！", null);

        // 双向添加好友，事务
        friendService.insertFriend(userName, name);
        friendService.insertFriend(name, userName);

        return RespBean.success("好友申请同意成功！", null);
    }

    @Override
    public RespBean refuse(String name, HttpServletRequest request) {
        // 登录验证
        String userName = (String) request.getSession().getAttribute("user");
        if (userName == null) return RespBean.error("非法访问！请登录！");

        // 参数验证
        if (name == null) return RespBean.error("名称不能为空！");

        // 验证拒绝的未回应的好友申请是否存在
        FriendApply friendApply = friendApplyMapper.getFriendApplyWithNotRespond(name, userName);
        if (friendApply == null) return RespBean.error("该好友申请不存在！");

        // 拒绝
        friendApply.setStatus(2);
        friendApplyMapper.updateById(friendApply);

        return RespBean.success("好友申请拒绝成功！", null);
    }

    // 好友申请
    private boolean apply(String name, String applyName, String msg) {
        // 好友申请的信息
        FriendApply friendApply = new FriendApply();
        friendApply.setName(name);
        friendApply.setApplyName(applyName);
        friendApply.setMsg(msg);

        // 进行好友申请
        Integer ret = friendApplyMapper.insert(friendApply);
        return ret != null && ret == 1;
    }

    // 查询是否申请过
    private boolean applyed(String name, String applyName) {
        Integer applyed = friendApplyMapper.applyed(name, applyName);
        return applyed != null && applyed == 1;
    }
}
