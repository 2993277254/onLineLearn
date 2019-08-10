package com.lsfly.websocket;

import com.lsfly.controller.system.BaseController;
import com.lsfly.controller.system.SysUserController;
import com.lsfly.sys.Result;
import com.lsfly.util.CommUtil;
import com.lsfly.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "webSocket")
public class MyWebSocketController extends BaseController {
    public static final Logger logger = LoggerFactory.getLogger(MyWebSocketController.class);
    @Bean//这个注解会从Spring容器拿出Bean
    public MyWebSocketHandler infoHandler() {

        return new MyWebSocketHandler();
    }
    //先判断用户登录
//    @RequestMapping(value = "/webSocket/login.do")
//    @ResponseBody
//    public Result login(HttpServletRequest request, HttpServletResponse response) throws Exception{
//        Result r=new Result();
//        if (ToolUtil.isEmpty(ToolUtil.getCurrentUserId())){
//            r.setRtnCode("1");//表示未登录
//        }
//        return r;
//    }
    @RequestMapping("/websocket/send.do")
    @ResponseBody
    public void send(HttpServletRequest request) {
//        Result r=new Result();
        infoHandler().sendMessageToUsers(new TextMessage("你好，测试！！！！"));
//        return r;
    }
}
