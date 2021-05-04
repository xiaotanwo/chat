package com.foxandgrapes.service.impl;

import com.foxandgrapes.pojo.Message;
import com.foxandgrapes.service.IPictureService;
import com.foxandgrapes.utils.MessageUtils;
import com.foxandgrapes.vo.RespBean;
import com.foxandgrapes.ws.ChatEndpoint;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class PictureServiceImpl implements IPictureService {

    @Override
    public RespBean upload(int msgType, String msgToName, MultipartFile file, HttpServletRequest request) {
        // 登录验证
        String userName = (String) request.getSession().getAttribute("user");
        if (userName == null) return RespBean.error("非法访问！请登录！");

        if (msgType == -1 || msgToName == null || "".equals(msgToName)) {
            return RespBean.error("请选择发送对象！");
        }

        // IDEA运行时的路径
        // String staticPath = ClassUtils.getDefaultClassLoader().getResource("static").getPath() + "/img";
        // 打包成jar时的路径
        ApplicationHome h = new ApplicationHome(getClass());
        File jarF = h.getSource();
        String staticPath = jarF.getParentFile() + "/img";

        // 获取文件在服务器的储存位置
        File filePath = new File(staticPath);
        if(!filePath.exists() && !filePath.isDirectory()){
            System.out.println("目录不存在，创建目录：" + filePath);
            filePath.mkdir();
        }

        //获取原始文件名称（包括格式）
        String originalFileName = file.getOriginalFilename();

        //获取文件类型，以最后一个‘.’为标识
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

        // 拒绝不是图片的上传
        if (!"jpg".equals(type)) {
            return RespBean.error("发送的不是JPG图片！");
        }

        //获取文件名称（不包含格式）
        String name = originalFileName.substring(0, originalFileName.lastIndexOf("."));

        //设置文件新名称：当前事件+文件名称（不包含格式）
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(d);

        // 原名称只取数字和字母
        String fileName = date + name.replaceAll("[^\\w]|_","") + "." + type;

        //在指定路径下创建文件
        File targetFile = new File(staticPath, fileName);

        //将文件保存到服务器指定位置
        try {
            file.transferTo(targetFile);
        } catch (IOException e){
            e.printStackTrace();
            return RespBean.error("图片上传失败！");
        }

        // 发送图片
        try {
            Message message = new Message();
            message.setFromName(userName);
            message.setObj(fileName);
            switch (msgType) {
                case 0:
                    message.setType(2);
                    message.setToName(msgToName);
                    Map<String, ChatEndpoint> map = ChatEndpoint.getOnlineUsers();
                    for (Map.Entry<String, ChatEndpoint> entry : map.entrySet()) {
                        ChatEndpoint ce = entry.getValue();
                        if (ce != null) {
                            ce.getSession().getBasicRemote().sendText(MessageUtils.getMessage(message));
                        }
                    }
                    break;
                case 1:
                    message.setType(16);
                    for (String toName : ChatEndpoint.getGroupOnlineUsers().get(msgToName)) {
                        message.setToName(msgToName);
                        ChatEndpoint ce = ChatEndpoint.getOnlineUsers().get(toName);
                        if (ce != null) {
                            ce.getSession().getBasicRemote().sendText(MessageUtils.getMessage(message));
                        }
                    }
                    break;
                case 2:
                    message.setType(29);
                    // 给对方
                    message.setToName(msgToName);
                    ChatEndpoint ce = ChatEndpoint.getOnlineUsers().get(msgToName);
                    if (ce != null) {
                        ce.getSession().getBasicRemote().sendText(MessageUtils.getMessage(message));
                    }
                    // 给自己
                    ce = ChatEndpoint.getOnlineUsers().get(userName);
                    if (ce != null) {
                        ce.getSession().getBasicRemote().sendText(MessageUtils.getMessage(message));
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("图片发送失败！");
        }

        return RespBean.success("图片发送成功！", null);
    }
}
