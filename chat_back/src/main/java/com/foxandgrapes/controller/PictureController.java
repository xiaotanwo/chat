package com.foxandgrapes.controller;

import com.foxandgrapes.service.IPictureService;
import com.foxandgrapes.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * <p>
 *  图片控制器
 * </p>
 *
 * @author tsk
 * @since 2021-04-20
 */

@RestController
@RequestMapping("/picture")
public class PictureController {

    @Autowired
    private IPictureService pictureService;

    /**
     * 上传图片
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public RespBean upload(@RequestParam(value="msgType") int msgType, @RequestParam(value="msgToName") String msgToName,
                           @RequestParam("file") MultipartFile file, HttpServletRequest request) {
        return pictureService.upload(msgType, msgToName, file, request);
    }
}
