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
@TableName("t_friend_apply")
public class FriendApply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户
     */
    private String name;

    /**
     * 申请的用户好友
     */
    private String applyName;

    /**
     * 好友申请状态，0未处理，1已接受，2已拒绝
     */
    private Integer status;

    /**
     * 申请好友的信息
     */
    private String msg;


}
