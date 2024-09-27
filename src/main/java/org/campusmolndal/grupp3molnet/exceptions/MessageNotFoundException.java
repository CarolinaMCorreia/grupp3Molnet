package org.campusmolndal.grupp3molnet.exceptions;

public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(Long messageId) {
        super("Could not find message with id: " + messageId);
    }

    public MessageNotFoundException(Long messageId, Throwable cause) {
        super("Could not find message with id: " + messageId, cause);
    }
}
