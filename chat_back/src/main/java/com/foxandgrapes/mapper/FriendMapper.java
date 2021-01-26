package com.foxandgrapes.mapper;

import com.foxandgrapes.pojo.Friend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tsk
 * @since 2021-01-22
 */
public interface FriendMapper extends BaseMapper<Friend> {

    // 查询好友列表
    List<String> getFriendList(String name);

    // 删除好友
    Integer deleteFriend(String friend, String name);

    // 判断是否为好友
    Integer isFriend(String name, String friend);
}
