package org.campusmolndal.grupp3molnet.exceptions;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UserAuthenticationException extends RuntimeException {

    private final String username;

    public UserAuthenticationException(String username, String message, Throwable cause) {
        super(message, cause);
        this.username = username;
    }

    public UserAuthenticationException(String username, String message) {
        super(message);
        this.username = username;
    }

    public UserAuthenticationException(String username) {
        super("Authentication failed for user: " + username);
        this.username = username;
    }
}
