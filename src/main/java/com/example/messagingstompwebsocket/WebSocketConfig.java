package com.example.messagingstompwebsocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.setApplicationDestinationPrefixes("/app");

		config.enableStompBrokerRelay("/topic")
		//.setRelayHost("b-33696072-fddd-4284-9531-177de64ade08-2.mq.us-east-2.amazonaws.com")
		//.setRelayPort(61614)
		//.setClientLogin("admin")
		//.setClientPasscode("justinrigon92");

		.setRelayHost("localhost")
		.setRelayPort(61613)
		.setClientPasscode("guest")
		.setClientLogin("guest");

		//config.enableSimpleBroker("/topic");

	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/gs-guide-websocket")
				.setHandshakeHandler(defaultHandshakeHandler())
				.setAllowedOrigins("*");

		registry.addEndpoint("/gs-guide-websocket")
				.setHandshakeHandler(defaultHandshakeHandler())
				.setAllowedOrigins("*")
				.withSockJS()
				.setInterceptors(httpSessionHandshakeInterceptor());
	}

	/*
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ChannelInterceptor() {
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				return null;
			}

			@Override
			public void postSend(Message<?> message, MessageChannel channel, boolean sent) {

			}

			@Override
			public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {

			}

			@Override
			public boolean preReceive(MessageChannel channel) {
				return false;
			}

			@Override
			public Message<?> postReceive(Message<?> message, MessageChannel channel) {
				return null;
			}

			@Override
			public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {

			}
		});
	}
	*/

	public static final String IP_ADDRESS = "IP_ADDRESS";

	@Bean
	public HandshakeInterceptor httpSessionHandshakeInterceptor() {
		return new HandshakeInterceptor() {

			@Override
			public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
				if (request instanceof ServletServerHttpRequest) {
					ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
					attributes.put(IP_ADDRESS, servletRequest.getRemoteAddress());
				}
				return true;
			}

			@Override
			public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

			}
		};
	}

	private DefaultHandshakeHandler defaultHandshakeHandler() {
		return new DefaultHandshakeHandler() {
			@Override
			protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
				Principal principal = request.getPrincipal();
				if (principal == null) {
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					authorities.add(new SimpleGrantedAuthority("anonymous"));
					principal = new AnonymousAuthenticationToken("WebsocketConfiguration", "anonymous", authorities);
				}
				return principal;
			}
		};
	}


}
