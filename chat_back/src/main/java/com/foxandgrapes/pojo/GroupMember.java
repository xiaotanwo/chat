package com.foxandgrapes.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author tsk
 * @since 2021-01-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_group_member")
public class GroupMember implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 群聊名称
     */
    private String groupName;

    /**
     * 用户
     */
    private String memberName;


}
