package com.example.messagingstompwebsocket.chat.controller;

import com.example.messagingstompwebsocket.dto.MessageResponseDTO;
import com.example.messagingstompwebsocket.dto.MessageDTO;
import com.example.messagingstompwebsocket.exceptions.PrivateForbiddenException;
import com.example.messagingstompwebsocket.exceptions.TopicForbiddenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;


@Controller
public class ChatController {

	private final static Logger logger = LoggerFactory.getLogger(ChatController.class);

	@SubscribeMapping("/messages")
	public MessageResponseDTO subscribeChat(Principal principal) {
		return null;
	}

	@SubscribeMapping("/queue/chat")
	@SendTo("/user/queue/chat")
	public MessageResponseDTO subscribeMyselfUser(Principal principal) {
		// Intercetto il subscribe dell'utente. Anche se non serve fare nulla, è già loggato
		return null;
	}

	// Protect for subscribe on other user
	@SubscribeMapping("/{username}/queue/chat")
	@SendTo("/user/{username}/queue/chat")
	public MessageResponseDTO subscribeGenericUser(@DestinationVariable("username") String username, Principal principal) {
		// In ogni caso, se user fa il subscribe sulla chat di user2, non riceve comunque i messaggi. Torniamo comunque un'eccezione
		if (username.equals(principal.getName())) {
			return null;
		} else {
			throw new PrivateForbiddenException("Access Denied");
		}
	}

	/*
		Topic messages - map /app/messages to /topic/messages
		Messages are delivered in broadcast across all nodes
	 */
	@MessageMapping("/messages")
	@SendTo("/topic/messages")
	public MessageResponseDTO chat(MessageDTO message, Principal principal) {
		logger.info("Topic message from {} - Payload: {}", principal.getName(), message);
		if (message.getName().equalsIgnoreCase("EXIT")) {
			throw new TopicForbiddenException("Access Denied");
		}
		//Thread.sleep(1000); // simulated delay
		return new MessageResponseDTO("Messaggio condiviso!! " + HtmlUtils.htmlEscape(message.getName()) + "! from user: " + principal.getName() );
	}



	@MessageMapping("/chat/user-{destinationUser}")
	@SendTo("/user/{destinationUser}/queue/chat")
	public MessageResponseDTO user(@DestinationVariable("destinationUser") String destinationUser, MessageDTO message, Principal principal) {
		logger.info("Private message from {} to {} - Payload: {}", principal.getName(), destinationUser, message);
		if (message.getName().equalsIgnoreCase("EXIT")) {
			throw new PrivateForbiddenException("Access Denied");
		}
		//Thread.sleep(1000); // simulated delay
		return new MessageResponseDTO("PRIVATE Message: , " + HtmlUtils.htmlEscape(message.getName()) + "! from user: " + principal.getName() );
	}


	@MessageExceptionHandler
	@SendToUser("/topic/errors")
	public String handleExceptionTopic(TopicForbiddenException exception) {
		logger.error("handleExceptionTopic() - {}", exception.getMessage());
		return exception.getMessage();
	}

	@MessageExceptionHandler
	@SendToUser("/queue/errors")
	public String handleExceptionPrivate(PrivateForbiddenException exception) {
		logger.error("handleExceptionPrivate() - {}", exception.getMessage());
		return exception.getMessage();
	}


}
