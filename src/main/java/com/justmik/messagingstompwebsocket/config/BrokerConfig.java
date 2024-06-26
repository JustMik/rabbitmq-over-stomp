package com.justmik.messagingstompwebsocket.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class BrokerConfig implements WebSocketMessageBrokerConfigurer {
    private Logger logger = LoggerFactory.getLogger(BrokerConfig.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(getChannelInterceptor());
    }

    @Bean
    public ChannelInterceptor getChannelInterceptor() {
        return new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    Object authorization = accessor.getFirstNativeHeader("Authorization");
                    logger.info("Inside ChannelInterceptor");
                    Authentication user = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("user", "user"));
                    accessor.setUser(user);
                }
                return message;
            }
        };
    }
}


