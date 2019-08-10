package com.lsfly.websocket;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;

import java.util.ArrayList;
import java.util.Map;

//创建websocket的处理类
public class MyWebSocketHandler implements WebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);

    // 保存所有的用户session
    private static final ArrayList<WebSocketSession> users = new ArrayList<WebSocketSession>();

    // 连接 就绪时
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        logger.info("connect websocket success......."+webSocketSession);
        users.add(webSocketSession);
    }
    // 处理信息
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        Gson gson = new Gson();

        // 将消息JSON格式通过Gson转换成Map
        // message.getPayload().toString() 获取消息具体内容
        Map<String, Object> msg = gson.fromJson(webSocketMessage.getPayload().toString(),
                new TypeToken<Map<String, Object>>() {}.getType());

        logger.info("handleMessage......."+webSocketMessage.getPayload()+"..........."+msg);

//		session.sendMessage(message);

        // 处理消息 msgContent消息内容
        TextMessage textMessage = new TextMessage(msg.get("msgContent").toString(), true);
        // 调用方法（发送消息给所有人）
        sendMsgToAllUsers(textMessage);
    }
    // 处理传输时异常
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }
    // 关闭 连接时
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    // 给所有用户发送 信息
    public void sendMsgToAllUsers(WebSocketMessage<?> message) throws Exception{

        for (WebSocketSession user : users) {
            user.sendMessage(message);
        }

    }

}
