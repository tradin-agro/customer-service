package com.example.api.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletException;
import javax.validation.ConstraintViolationException;

import java.io.IOException;
import java.util.Date;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiErrorResponse handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), new Date(),
                request.getDescription(false), true);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ApiErrorResponse handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        return new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), new Date(),
                request.getDescription(false), false);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpClientErrorException.class)
    public ApiErrorResponse handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), new Date(),
                request.getDescription(false), false);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(JsonProcessingException.class)
    public ApiErrorResponse handleJsonProcessingException(JsonProcessingException ex, WebRequest request) {
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), new Date(),
                request.getDescription(false), false);
    }


    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        String[] messageParts = ex.getMessage().split(":");
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), messageParts[0].trim(), new Date(),
                request.getDescription(false), false);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ApiErrorResponse handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex, WebRequest request) {
        String[] messageParts = ex.getMessage().split(":");
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), messageParts[1].trim(), new Date(),
                request.getDescription(false), false);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({EntityNotFoundException.class, NoHandlerFoundException.class})
    public ApiErrorResponse handleFourZeroFour(Exception ex, WebRequest request) {
        return new ApiErrorResponse(NOT_FOUND.value(), ex.getMessage(), new Date(),
                request.getDescription(false), false);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ApiErrorResponse handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        return new ApiErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), new Date(),
                request.getDescription(false), false);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ApiErrorResponse handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex, WebRequest request) {
        return new ApiErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Usuário inexistente ou senha inválida", new Date(),
                request.getDescription(false), false);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServletException.class)
    public ApiErrorResponse handlegServletException(ServletException ex, WebRequest request) {
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), new Date(),
                request.getDescription(false), false);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ApiErrorResponse handlegRuntimeException(RuntimeException ex, WebRequest request) {
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), new Date(),
                request.getDescription(false), false);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public ApiErrorResponse handlegIOException(IOException ex, WebRequest request) {
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), new Date(),
                request.getDescription(false), false);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiErrorResponse handlegException(Exception ex, WebRequest request) {
        return new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), new Date(),
                request.getDescription(false), false);
    }
}

