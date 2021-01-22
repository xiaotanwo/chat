package com.foxandgrapes.service.impl;

import com.foxandgrapes.pojo.Group;
import com.foxandgrapes.mapper.GroupMapper;
import com.foxandgrapes.service.IGroupService;
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
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {

}
