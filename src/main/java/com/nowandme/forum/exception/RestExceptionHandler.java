package com.nowandme.forum.exception;

import com.nowandme.forum.model.dao.MaliciousRequestException;
import com.nowandme.forum.model.error.ApiError;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        log.error(error);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationFailure(AuthenticationException ex) {
        log.error(ex.getMessage(), ex.getCause());
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler({JwtException.class, MaliciousRequestException.class})
    protected ResponseEntity<Object> handleJwtException(Exception ex) {
        log.error(ex.getMessage(), ex.getCause());
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(SQLException.class)
    protected ResponseEntity<Object> handleSqlFailure(SQLException ex) {
        log.error(ex.getMessage(), ex.getCause());
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    protected ResponseEntity<Object> handleRecordNotPresent(RecordNotFoundException ex) {
        log.error(ex.getMessage(), ex.getCause());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }
}
