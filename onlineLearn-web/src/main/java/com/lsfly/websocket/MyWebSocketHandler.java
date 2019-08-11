package com.lsfly.websocket;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lsfly.exception.InvalidParamException;
import com.lsfly.exception.RtnCode;
import com.lsfly.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//创建websocket的处理类
public class MyWebSocketHandler implements WebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);

    // 保存所有的用户session
    private static final Map<String, WebSocketSession> users;  //Map来存储WebSocketSession，key用USER_ID 即在线用户列表

    //用户标识
    private static final String USER_ID = "WEBSOCKET_USERID";   //对应监听器从的key

    static {
        users=new HashMap<String, WebSocketSession>();
    }
    // 连接 就绪时
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        logger.info("成功建立WebSocket连接");
        String userId=(String) webSocketSession.getAttributes().get(USER_ID);
        users.put(userId,webSocketSession);
        logger.info("当前线上用户数量:"+users.size());
    }

    // 处理信息
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        Gson gson = new Gson();

        // 将消息JSON格式通过Gson转换成Map
        // message.getPayload().toString() 获取消息具体内容
//        Map<String, Object> msg = gson.fromJson(webSocketMessage.getPayload().toString(),
//                new TypeToken<Map<String, Object>>() {}.getType());

//        logger.info("handleMessage......."+webSocketMessage.getPayload()+"..........."+msg);

//		session.sendMessage(message);

        // 处理消息 msgContent消息内容
        TextMessage textMessage = new TextMessage(webSocketMessage.getPayload().toString(), true);
        // 调用方法（发送消息给所有人）
        sendMessageToUsers(textMessage);
    }
    // 处理传输时异常
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        if (webSocketSession.isOpen()){
            webSocketSession.close();
        }
        logger.info("传输出现异常，关闭websocket连接... ");
        String userId= (String) webSocketSession.getAttributes().get(USER_ID);
        users.remove(userId);

    }
    // 关闭 连接时
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        logger.info("关闭websocket连接");
        String userId= (String) webSocketSession.getAttributes().get(USER_ID);
        System.out.println("用户"+userId+"已退出！");
        users.remove(userId);
        logger.info("剩余在线用户"+users.size());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    //给某个用户发送消息
    public void sendMessageToUser(String userId, TextMessage message){

        for (String id:users.keySet()){
            if (id.equals(userId)){
                try {
                    if (users.get(id).isOpen()){
                        users.get(id).sendMessage(message);
                    }
                }catch (IOException e){
//                    e.printStackTrace();
                    throw new InvalidParamException("请先登录",RtnCode.NO_LOIN);
                }
            }
        }
    }

    // 给所有在线用户发送 信息
    public void sendMessageToUsers(TextMessage message) {

        int i=0;
        for (String userId : users.keySet()) {
            try {
                if (users.get(userId).isOpen()) {//自己本身不用推送信息
                    users.get(userId).sendMessage(message);
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
//                throw new InvalidParamException("请先登录",RtnCode.NO_LOIN);
            }

        }
        logger.info("发送的个数为"+i);
    }
}
