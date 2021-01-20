package com.foxandgrapes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foxandgrapes.mapper.UserMapper;
import com.foxandgrapes.pojo.User;
import com.foxandgrapes.service.IUserService;
import com.foxandgrapes.utils.MD5Util;
import com.foxandgrapes.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @Override
    public RespBean login(User user, HttpServletRequest request) {
        if (user == null) return RespBean.error("用户信息不能为空！");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", user.getName());

        List<User> list = userMapper.selectList(queryWrapper);
        if (list.size() != 1) {
            return RespBean.error("用户不存在！请检查昵是否有误！");
        }

        User querryUser = list.get(0);
        if (!querryUser.getPassword().equals(MD5Util.fromPassToDBPass(user.getPassword(), querryUser.getSalt()))) {
            return RespBean.error("密码错误！请检查密码是否有误！");
        }

        // 登录成功
        request.setAttribute("user", user);
        return RespBean.success("登录成功！", null);
    }

    @Override
    public RespBean register(User user, HttpServletRequest request) {
        if (user == null) return RespBean.error("用户信息不能为空！");

        int nameLen = user.getName().length();
        int passwordLen = user.getPassword().length();
        if (nameLen < 2 || nameLen > 10 || passwordLen != 32) {
            return RespBean.error("用户信息不合法！请按规定填写！");
        }

        // 查询是否已有用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", user.getName());

        List<User> list = userMapper.selectList(queryWrapper);
        if (list.size() >= 1) {
            return RespBean.error("用户已存在！请更改昵称！");
        }

        // 第二次MD5
        String salt = MD5Util.getRandomSalt();
        user.setSalt(salt);
        user.setPassword(MD5Util.fromPassToDBPass(user.getPassword(), salt));

        int ret = userMapper.insert(user);
        if (ret != 1) return RespBean.error("注册失败！");

        // 注册成功
        request.setAttribute("user", user);
        return RespBean.success("注册成功！", null);
    }
}
