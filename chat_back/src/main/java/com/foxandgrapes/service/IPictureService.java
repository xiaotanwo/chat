package com.foxandgrapes.service;

import com.foxandgrapes.vo.RespBean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  图片类
 * </p>
 *
 * @author tsk
 * @since 2021-04-20
 */
public interface IPictureService {

    /**
     * 上传图片
     * @param msgType
     * @param msgToName
     * @param file
     * @param request
     * @return
     */
    RespBean upload(int msgType, String msgToName, MultipartFile file, HttpServletRequest request);
}
