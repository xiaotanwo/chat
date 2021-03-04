package com.foxandgrapes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foxandgrapes.mapper.UserMapper;
import com.foxandgrapes.pojo.User;
import com.foxandgrapes.service.IFriendService;
import com.foxandgrapes.service.IGroupMemberService;
import com.foxandgrapes.service.IUserService;
import com.foxandgrapes.utils.MD5Util;
import com.foxandgrapes.vo.RespBean;
import com.foxandgrapes.ws.ChatEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tsk
 * @since 2021-01-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IFriendService friendService;
    @Autowired
    private IGroupMemberService groupMemberService;

    @Override
    public RespBean login(User user, HttpServletRequest request) {
        // 参数验证
        if (user == null) return RespBean.error("用户信息不能为空！");

        // 获取用户
        User querryUser = getUserByName(user.getName());
        if (querryUser == null) return RespBean.error("用户不存在！请检查昵是否有误！");

        // 密码判断
        if (!querryUser.getPassword().equals(MD5Util.fromPassToDBPass(user.getPassword(), querryUser.getSalt()))) {
            return RespBean.error("密码错误！请检查密码是否有误！");
        }

        // 登录成功，在session中保存用户名字
        request.getSession().setAttribute("user", user.getName());

        // 初始化该用户的所有信息
        init(user.getName());

        return RespBean.success("登录成功！", null);
    }

    private void init(String userName) {
        // 获取好友并保存
        friendService.getFriends(userName);
        // 获取群聊并保存
        groupMemberService.getGroups(userName);
    }

    @Override
    public RespBean register(User user, HttpServletRequest request) {
        // 参数判断
        if (user == null) return RespBean.error("用户信息不能为空！");

        // 注册信息验证
        int nameLen = user.getName().length();
        int passwordLen = user.getPassword().length();
        if (nameLen < 2 || nameLen > 10 || passwordLen != 32) return RespBean.error("用户信息不合法！请按规定填写！");

        // 查询是否已有用户
        if (isUser(user.getName())) return RespBean.error("用户已存在！请更改昵称！");

        // 注册
        if (!register(user.getName(), user.getPassword())) return RespBean.error("注册失败！");

        // 注册成功，在session中保存用户名字
        request.getSession().setAttribute("user", user.getName());

        // 初始化该用户的所有信息
        init(user.getName());

        return RespBean.success("注册成功！", null);
    }

    @Override
    public RespBean logout(HttpServletRequest request) {
        String userName = (String) request.getSession().getAttribute("user");
        request.getSession().removeAttribute("user");
        // 移除对应的好友列表
        ChatEndpoint.getAllFriends().remove(userName);
        // 移除对应的群聊列表
        ChatEndpoint.getAllGroups().remove(userName);
        return RespBean.success("退出成功！", null);
    }

    // 通过用户名获取
    @Override
    public User getUserByName(String name) {
        return userMapper.getUserByName(name);
    }

    // 判断用户是否存在
    @Override
    public boolean isUser(String name) {
        User friend = getUserByName(name);
        return friend != null;
    }

    // 注册
    private boolean register(String userName, String password) {
        // 用户信息
        User user = new User();
        // 第二次MD5，随机盐值
        String salt = MD5Util.getRandomSalt();
        user.setName(userName);
        user.setSalt(salt);
        user.setPassword(MD5Util.fromPassToDBPass(password, salt));

        Integer ret = userMapper.insert(user);
        return ret != null && ret == 1;
    }
}
