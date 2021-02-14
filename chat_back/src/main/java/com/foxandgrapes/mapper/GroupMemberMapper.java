package com.foxandgrapes.mapper;

import com.foxandgrapes.pojo.GroupMember;
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
public interface GroupMemberMapper extends BaseMapper<GroupMember> {

    // 获取群聊
    List<String> getGroups(String name);

    // 判断是否在群聊中
    Integer inGroup(String groupName, String name);

    // 删除群聊
    Integer deleteGroup(String groupName, String name);
}
