package ru.fedorichev.diplom.exception;

import javax.security.auth.message.AuthException;

public class UserAuthException extends RuntimeException {
    public UserAuthException(AuthException e) {
        super(e.getMessage());
    }
}
