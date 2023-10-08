package com.iceservices.musicmetadataservice.api;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.iceservices.musicmetadataservice.exception.ArtistNotFoundException;
import com.iceservices.musicmetadataservice.exception.ArtistOfTheDayNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

@ControllerAdvice
@Slf4j
public class GlobalWebErrorHandler {

    @ExceptionHandler(Exception.class)
    protected final ResponseEntity<ErrorResponse> handle(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        ErrorResponse errorResponse = ErrorResponse.create(exception, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error happened");
        log.error("Handle unexpected exception {}", Map.of("response", errorResponse), exception);
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected final ResponseEntity<ErrorResponse> handle(HttpServletRequest request, HttpServletResponse response, NoHandlerFoundException exception) {
        ErrorResponse errorResponse = ErrorResponse.create(exception, HttpStatus.NOT_FOUND, format("Path '%s' does not exist", exception.getRequestURL()));
        log.warn("Handle no handler found exception {}", Map.of("response", errorResponse), exception);
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    private ResponseEntity<ErrorResponse> handle(HttpServletRequest request, BindException exception) {
        ErrorResponse errorResponse = ErrorResponse.create(exception, HttpStatus.BAD_REQUEST, getErrorCodes(exception.getBindingResult()).toString());
        log.warn("Handle invalid request exception {}", Map.of("response", errorResponse), exception);
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(InvalidFormatException.class)
    private ResponseEntity<ErrorResponse> handle(HttpServletRequest request, InvalidFormatException exception) {
        ErrorResponse errorResponse = ErrorResponse.create(exception, HttpStatus.BAD_REQUEST, format(ENGLISH, "Value '%s' does not have a valid format", exception.getValue()));
        log.warn("Handle invalid format exception {}", Map.of("response", errorResponse), exception);
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ErrorResponse> handle(HttpServletRequest request, HttpMessageNotReadableException exception) {
        ErrorResponse errorResponse = ErrorResponse.create(exception, HttpStatus.BAD_REQUEST, "The body is not readable");
        log.warn("Handle invalid request exception {}", Map.of("response", errorResponse), exception);
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<ErrorResponse> handle(HttpServletRequest request, HttpRequestMethodNotSupportedException exception) {
        ErrorResponse errorResponse = ErrorResponse.create(exception, HttpStatus.METHOD_NOT_ALLOWED, format(ENGLISH, "Method '%s' not allowed for path '%s'", exception.getMethod(), getPath(request)));
        log.warn("Handle method not supported exception {}", Map.of("response", errorResponse), exception);
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<ErrorResponse> handle(HttpServletRequest request, MethodArgumentTypeMismatchException exception) {
        ErrorResponse errorResponse = ErrorResponse.create(exception, HttpStatus.METHOD_NOT_ALLOWED, format(ENGLISH, "Request value type mismatch on field '%s'", exception.getName()));
        log.warn("Handle method argument type mismatch exception {}", Map.of("response", errorResponse), exception);
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(ArtistNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handle(HttpServletRequest request, HttpServletResponse response, ArtistNotFoundException exception) {
        ErrorResponse errorResponse = ErrorResponse.create(exception, HttpStatus.NOT_FOUND, format("Artist with id '%s' does not exist", exception.getId()));
        log.error("Handle no artist found exception {}", Map.of("response", errorResponse), exception);
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(ArtistOfTheDayNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handle(HttpServletRequest request, HttpServletResponse response, ArtistOfTheDayNotFoundException exception) {
        ErrorResponse errorResponse = ErrorResponse.create(exception, HttpStatus.NOT_FOUND, "No artist of the day found");
        log.error("Handle no artist found exception {}", Map.of("response", errorResponse), exception);
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    private List<String> getErrorCodes(BindingResult bindingResult) {
        final Stream<String> fieldErrors = bindingResult.getFieldErrors().stream().map(violation -> format(ENGLISH, "Field '%s' %s. Value: '%s'", violation.getField(), violation.getDefaultMessage(), violation.getRejectedValue()));
        final Stream<String> globalErrors = bindingResult.getGlobalErrors().stream().map(violation -> format(ENGLISH, "Error in '%s'; %s.", violation.getObjectName(), violation.getDefaultMessage()));

        return Stream.concat(fieldErrors, globalErrors).collect(Collectors.toList());
    }

    private String getPath(HttpServletRequest request) {
        String path = Optional.ofNullable(request.getServletPath()).filter(s -> !s.isEmpty()).orElse(request.getRequestURI());
        return Optional.ofNullable(path).orElse(request.getPathInfo());
    }
}
