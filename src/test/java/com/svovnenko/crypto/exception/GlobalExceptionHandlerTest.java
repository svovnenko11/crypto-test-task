package com.svovnenko.crypto.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svovnenko.crypto.domain.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest mockRequest;

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHandleException_InternalServerError() throws Exception {
        Exception exception = new Exception("Internal Server Error");

        ErrorResponseDto expectedResponse = new ErrorResponseDto()
                .timestamp(Instant.now())
                .message(List.of(exception.getMessage()));

        var responseEntity = handler.handleException(exception, mockRequest);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(500);
        assertThat(responseEntity.getBody()).usingRecursiveComparison()
                .ignoringFields("timestamp").isEqualTo(expectedResponse);
    }

    @Test
    void testHandleNotFound_NotFoundError() throws Exception {
        CryptoNotFoundException exception = new CryptoNotFoundException("Resource not found");

        ErrorResponseDto expectedResponse = new ErrorResponseDto()
                .timestamp(Instant.now())
                .message(List.of(exception.getMessage()));

        var responseEntity = handler.handleNotFound(exception, mockRequest);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(404);
        assertThat(responseEntity.getBody()).usingRecursiveComparison()
                .ignoringFields("timestamp").isEqualTo(expectedResponse);
    }

    @Test
    void testHandleMissingServletRequestParameter_BadRequest() throws Exception {
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException("param", "String");

        ErrorResponseDto expectedResponse = new ErrorResponseDto()
                .timestamp(Instant.now())
                .message(List.of("Required parameter 'param' is not present."));

        var responseEntity = handler.handleMissingServletRequestParameter(exception, mockRequest);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(400);
        assertThat(responseEntity.getBody()).usingRecursiveComparison()
                .ignoringFields("timestamp").isEqualTo(expectedResponse);
    }

    @Test
    void testHandleWrongDatesOrder_BadRequest() throws Exception {
        String errorMessage = "Start date must be before end date";
        WrongDatesOrderException exception = new WrongDatesOrderException(errorMessage);

        ErrorResponseDto expectedResponse = new ErrorResponseDto()
                .timestamp(Instant.now())
                .message(List.of(errorMessage));

        var responseEntity = handler.handleWrongDatesOrder(exception, mockRequest);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(400);
        assertThat(responseEntity.getBody()).usingRecursiveComparison()
                .ignoringFields("timestamp").isEqualTo(expectedResponse);
    }

    @Test
    void testHandleMethodArgumentTypeMismatch_BadRequest() throws Exception {
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("id");
        when(exception.getValue()).thenReturn("invalid");

        ErrorResponseDto expectedResponse = new ErrorResponseDto()
                .timestamp(Instant.now())
                .message(List.of("Invalid parameter 'id': invalid "));

        var responseEntity = handler.handleMethodArgumentTypeMismatch(exception, mockRequest);

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(400);
        assertThat(responseEntity.getBody()).usingRecursiveComparison()
                .ignoringFields("timestamp").isEqualTo(expectedResponse);
    }
}