package com.lsfly.websocket;

import com.lsfly.controller.courseUser.TCourseUserController;
import com.lsfly.util.CommUtil;
import com.lsfly.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;


//创建握手（handshake）接口/拦截器
//这个的主要作用是可以在握手前做一些事，
// 把所需要的东西放入到attributes里面，
// 然后可以在WebSocketHandler的session中，
// 取到相应的值，具体可参考HttpSessionHandshakeInterceptor，
// 这儿也可以实现HandshakeInterceptor 接口。

public class MyHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    public static final Logger logger = LoggerFactory.getLogger(MyHandshakeInterceptor.class);

    // 握手前
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        System.out.println("++++++++++++++++ HandshakeInterceptor: beforeHandshake  ++++++++++++++"+attributes);
        logger.info("握手前判断用户登录");
//        握手之前将登陆用户信息从session设置到WebSocketSession
        if (request instanceof ServletServerHttpRequest){
            String userId=ToolUtil.getCurrentUserId();
            if (ToolUtil.isNotEmpty(userId)){
//                使用userId区分WebSocketHandler，以便定向发送消息
                attributes.put("WEBSOCKET_USERID",userId);
                return true;
            }else {
                logger.info("握手前判断用户未登录");
                return false;
            }
        }
        return true;
    }



    // 握手后
    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        logger.info("握手之后");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
