package com.example.messagingstompwebsocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;


@Controller
public class GreetingController {

	@Value("${server.port}")
	private String port;
	private final ObjectMapper objectMapper;
	private final SimpMessagingTemplate template;

	public GreetingController(ObjectMapper objectMapper, SimpMessagingTemplate template) {
		this.objectMapper = objectMapper;
		this.template = template;
	}

	/*
		Topic messages - map /app/messages to /topic/messages
		Messages are delivered in broadcast across all nodes
	 */
	@MessageMapping("/messages")
	//@SendTo("/topic/private")
	public Greeting chat(HelloMessage message, Principal principal) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting("Messaggio condiviso!! " + HtmlUtils.htmlEscape(message.getName()) + "! - port [" + port + "] from user: " + principal.getName() );
	}

	/*
    Topic messages - map /app/messages to /topic/messages
    Messages are delivered in broadcast across all nodes
 */

	@MessageMapping("/chat/user-{destinationUser}")
	@SendTo("/user/{destinationUser}/queue/chat")
	public Greeting user(@DestinationVariable("destinationUser") String destinationUser, HelloMessage message, Principal principal) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting("PRIVATE Message: , " + HtmlUtils.htmlEscape(message.getName()) + "! - port [" + port + "] from user: " + principal.getName() );
	}



}
