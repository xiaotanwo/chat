package com.foxandgrapes.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private Boolean ret;
    private String msg;
    private Object obj;

    public static RespBean success(String msg, Object obj) {
        return new RespBean(true, msg, obj);
    }

    public static RespBean error(String msg) {
        return new RespBean(false, msg, null);
    }
}
