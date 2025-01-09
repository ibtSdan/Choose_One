package com.example.choose_one.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker

public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /topic 으로 시작하는 메세지는 메세지 브로커가 처리한다
        registry.enableSimpleBroker("/topic");

        // /app 으로 시작하는 메세지는 애플리케이션에서 처리
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // /chat 으로 클라이언트가 websocket 연결을 시작 가능
        registry.addEndpoint("/ws").withSockJS();
    }
}
