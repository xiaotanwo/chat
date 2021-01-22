package com.foxandgrapes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foxandgrapes.mapper.FriendApplyMapper;
import com.foxandgrapes.pojo.FriendApply;
import com.foxandgrapes.pojo.User;
import com.foxandgrapes.service.IFriendApplyService;
import com.foxandgrapes.service.IUserService;
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
public class FriendApplyServiceImpl extends ServiceImpl<FriendApplyMapper, FriendApply> implements IFriendApplyService {

    @Autowired
    private IUserService userService;
    @Autowired
    private FriendApplyMapper friendApplyMapper;

    @Override
    public RespBean add(FriendApply friendApply, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) return RespBean.error("非法访问！请登录！");

        if (friendApply == null) return RespBean.error("好友申请不能为空！");

        User friend = userService.getUserByName(friendApply.getApplyName());
        if (friend == null) return RespBean.error("该好友不存在！");

        // 为了防止前端的参数有误进行的设值
        friendApply.setId((long) 12); // 测试id有值的情况。
        friendApply.setName(user.getName());
        friendApply.setState(null);

        int ret = friendApplyMapper.insert(friendApply);
        if (ret != 1) {
            return RespBean.error("好友申请失败！");
        }

        return RespBean.success("好友申请成功！", null);
    }
}
