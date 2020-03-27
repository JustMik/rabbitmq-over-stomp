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

	@Value("${node.port}")
	private String port;
	private final ObjectMapper objectMapper;
	private final SimpMessagingTemplate template;

	public GreetingController(ObjectMapper objectMapper, SimpMessagingTemplate template) {
		this.objectMapper = objectMapper;
		this.template = template;
	}

	/*
	@MessageMapping("/hello/{chatId}")
	@SendTo("/topic/chat.{chatId}")
	@ExceptionHandler()
	public Greeting user2(@DestinationVariable String chatId,
						 HelloMessage message) throws Exception {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "! - port [" + port + "] - group: " + chatId + " - id: " + username);
	}

	@MessageMapping("/hello")
	@SendToUser("/topic/user") //.{userId}
	//@ExceptionHandler(value = )
	public Greeting chat(@DestinationVariable("group") String group,
						  @DestinationVariable("id") int id,
						  HelloMessage message) throws Exception {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "! - port [" + port + "] - group: " + group + " - id: " + id);
	}

	 */

	@MessageMapping("/messages/{group}/{id}")
	@SendTo("/topic/group.{group}.id.{id}")
	//@ExceptionHandler()
	public Greeting user(@DestinationVariable("group") String group,
						 @DestinationVariable("id") int id,
						 HelloMessage message) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "! - port [" + port + "] - group: " + group + " - id: " + id);
	}

	@MessageMapping("/messages/{group}")
	@SendTo("/topic/group.{group}")
	public Greeting group(@DestinationVariable("group") String group, HelloMessage message) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "! - port [" + port + "] - group: " + group );
	}

	/*
	@MessageMapping("/messages")
	@SendToUser("/topic/message")
	public Greeting messageByUser(HelloMessage message, Principal principal) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "! - port [" + port + "]" );
	}
	 */

}
