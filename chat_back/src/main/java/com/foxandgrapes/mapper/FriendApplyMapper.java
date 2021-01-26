package com.foxandgrapes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foxandgrapes.pojo.FriendApply;
import com.foxandgrapes.vo.FriendApplyVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tsk
 * @since 2021-01-22
 */
public interface FriendApplyMapper extends BaseMapper<FriendApply> {

    // 判断是否已经申请过，null为否，1为是
    Integer applyed(String name, String applyName);

    // 获取未回应的好友申请列表
    List<FriendApplyVo> getFriendApplyListWithNotRespond(String name);

    // 获取指定名称的未回应的好友申请
    FriendApply getFriendApplyWithNotRespond(String name, String applyName);
}
