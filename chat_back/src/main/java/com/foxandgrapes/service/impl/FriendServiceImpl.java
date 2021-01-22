package com.foxandgrapes.service.impl;

import com.foxandgrapes.pojo.Friend;
import com.foxandgrapes.mapper.FriendMapper;
import com.foxandgrapes.service.IFriendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tsk
 * @since 2021-01-22
 */
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements IFriendService {

}
