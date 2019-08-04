package com.lsfly.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class MyWebSocketConfig  extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    public static final Logger logger = LoggerFactory.getLogger(MyWebSocketConfig.class);

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //前台 可以使用websocket环境
        registry.addHandler(myWebSocketHandler(),"/websocket.action").addInterceptors(new MyHandshakeInterceptor());
        //前台 不可以使用websocket环境，则使用sockjs进行模拟连接
        registry.addHandler(myWebSocketHandler(), "/sockjs/websocket.action").addInterceptors(new MyHandshakeInterceptor()).withSockJS();
    }

    // websocket 处理类
    @Bean
    public WebSocketHandler myWebSocketHandler(){
        return new MyWebSocketHandler();
    }
}
