package com.example.messagingstompwebsocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
//@EnableScheduling
//@Profile("scheduler")
public class GreetingService {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate template;

    public GreetingService(ObjectMapper objectMapper, SimpMessagingTemplate template) {
        this.objectMapper = objectMapper;
        this.template = template;
    }

    @Scheduled(fixedRateString = "2000")
    public void greeting() throws JsonProcessingException {
        this.template.convertAndSend("/topic/group",
                objectMapper.writeValueAsString(new Greeting("Hello periodic message!")));
    }

}
