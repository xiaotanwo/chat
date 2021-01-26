package com.foxandgrapes.mapper;

import com.foxandgrapes.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tsk
 * @since 2021-01-19
 */
public interface UserMapper extends BaseMapper<User> {

    // 通过用户名获取用户
    User getUserByName(String name);
}
