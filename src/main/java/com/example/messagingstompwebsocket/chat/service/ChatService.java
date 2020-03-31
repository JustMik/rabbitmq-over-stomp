package com.example.messagingstompwebsocket.chat.service;

import com.example.messagingstompwebsocket.dto.MessageResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
//@EnableScheduling
//@Profile("scheduler")
public class ChatService {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate template;

    public ChatService(ObjectMapper objectMapper, SimpMessagingTemplate template) {
        this.objectMapper = objectMapper;
        this.template = template;
    }

    @Scheduled(fixedRateString = "2000")
    public void greeting() throws JsonProcessingException {
        this.template.convertAndSend("/user/user2/queue/chat",
                objectMapper.writeValueAsString(new MessageResponseDTO("Hello periodic message!")));
    }

}
