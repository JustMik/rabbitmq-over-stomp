package com.example.messagingstompwebsocket.exceptions;

public class TopicForbiddenException extends RuntimeException {

    public TopicForbiddenException(String message) {
        super(message);
    }
}
