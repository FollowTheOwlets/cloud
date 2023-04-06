package ru.fedorichev.diplom.exception;

import javax.security.auth.message.AuthException;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

}
