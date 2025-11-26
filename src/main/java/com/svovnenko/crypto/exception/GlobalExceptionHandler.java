package com.svovnenko.crypto.exception;

import com.svovnenko.crypto.domain.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatch(
            final MethodArgumentTypeMismatchException ex, final HttpServletRequest request) {
        String message = String.format("Invalid parameter '%s': %s ", ex.getName(), ex.getValue());
        log.debug(message);
        return buildValidationError(message, request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponseDto> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.debug(ex.getMessage());
        return buildValidationError(ex.getBody().getDetail(), request);
    }

    @ExceptionHandler(WrongDatesOrderException.class)
    protected ResponseEntity<ErrorResponseDto> handleWrongDatesOrder(
            WrongDatesOrderException ex, HttpServletRequest request) {
        log.debug(ex.getMessage());
        return buildValidationError(ex.getMessage(), request);
    }

    @ExceptionHandler(CryptoNotFoundException.class)
    protected ResponseEntity<ErrorResponseDto> handleNotFound(CryptoNotFoundException ex, HttpServletRequest request) {
        log.debug(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponseDto()
                        .timestamp(Instant.now())
                        .message(List.of(ex.getMessage())),
                getHeaders(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseDto> handleException(Exception ex, HttpServletRequest request) {
        String message = Optional.ofNullable(ex.getMessage()).orElse("Something went wrong");
        log.error(message, ex);
        return new ResponseEntity<>(
                new ErrorResponseDto()
                        .timestamp(Instant.now())
                        .message(List.of(message)),
                getHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<ErrorResponseDto> buildValidationError(String errorDetails, HttpServletRequest request) {
        return buildValidationError(List.of(errorDetails), request);
    }
    private ResponseEntity<ErrorResponseDto> buildValidationError(List<String> errorDetails, HttpServletRequest request) {
        return new ResponseEntity<>(
                new ErrorResponseDto()
                        .timestamp(Instant.now())
                        .message(errorDetails),
                getHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }


    private HttpHeaders getHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_PROBLEM_JSON_VALUE);
        return new HttpHeaders(headers);
    }
}
