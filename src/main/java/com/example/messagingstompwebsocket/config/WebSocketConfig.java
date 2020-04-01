package com.example.messagingstompwebsocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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

	@Autowired
	private Environment environment;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableStompBrokerRelay("/topic", "/queue")
				.setUserRegistryBroadcast("/topic/sync-users")
				.setRelayHost(environment.getProperty("application.rabbitmq.host"))
				.setRelayPort(environment.getProperty("application.rabbitmq.port", Integer.class))
				.setClientPasscode(environment.getProperty("application.rabbitmq.passcode"))
				.setClientLogin(environment.getProperty("application.rabbitmq.login"))
				.setSystemLogin(environment.getProperty("application.rabbitmq.login"))
				.setSystemPasscode(environment.getProperty("application.rabbitmq.passcode"));
		config.setApplicationDestinationPrefixes("/app", "/topic", "/user");
		// Il traffico su questi prefissi viene gestito dai controller a livello applicativo
		// @SubscribeMapping e @MessageMapping
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket")
				.setHandshakeHandler(defaultHandshakeHandler())
				.setAllowedOrigins("*")
				.withSockJS()
				.setInterceptors(httpSessionHandshakeInterceptor())
				.setClientLibraryUrl("/webjars/sockjs-client/1.0.2/sockjs.min.js");
	}

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
