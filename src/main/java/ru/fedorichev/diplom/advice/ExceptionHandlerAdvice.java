package ru.fedorichev.diplom.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.fedorichev.diplom.exception.FileStorageException;
import ru.fedorichev.diplom.exception.UnauthorizedException;
import ru.fedorichev.diplom.exception.UserAuthException;
import ru.fedorichev.diplom.component.Error;

import java.util.concurrent.atomic.AtomicInteger;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    private AtomicInteger id;

    public ExceptionHandlerAdvice() {
        id = new AtomicInteger(0);
    }

    @ExceptionHandler(UserAuthException.class)
    public ResponseEntity<Error> handler(UserAuthException e) {
        return buildResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Error> handler(UnauthorizedException e) {
        return buildResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Error> handler(MissingServletRequestParameterException e) {
        return buildResponse("Некорректные параметры запроса", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Error> handler(HttpMessageNotReadableException e) {
        return buildResponse("Некорректное тело запроса", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<Error> handler(FileStorageException e) {
        return buildResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Error> buildResponse(String msg, HttpStatus status) {
        switch (status) {
            case BAD_REQUEST:
                return new ResponseEntity<>(
                        new Error(
                                "BAD_REQUEST : " + msg,
                                id.incrementAndGet()
                        ),
                        HttpStatus.BAD_REQUEST
                );
            case UNAUTHORIZED:
                return new ResponseEntity<>(
                        new Error(
                                "UNAUTHORIZED : " + msg,
                                id.incrementAndGet()
                        ),
                        HttpStatus.UNAUTHORIZED
                );
            case INTERNAL_SERVER_ERROR:
                return new ResponseEntity<>(
                        new Error(
                                "INTERNAL_SERVER_ERROR : " + msg,
                                id.incrementAndGet()
                        ),
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            default:
                return new ResponseEntity<>(
                        new Error("BAD_GATEWAY", id.incrementAndGet()),
                        HttpStatus.BAD_GATEWAY
                );
        }
    }
}
