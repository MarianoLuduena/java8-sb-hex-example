package com.redbee.payments.config;

import com.redbee.payments.config.exception.BusinessException;
import com.redbee.payments.config.exception.NotFoundException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    private final HttpServletRequest httpServletRequest;

    public ErrorHandler(final HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiErrorResponse> handleDefault(Throwable ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        final ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, errorCode.getReasonPhrase(), errorCode);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(ConstraintViolationException ex) {
        log.error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex);
        final String message =
                ex.getConstraintViolations().stream().findFirst()
                        .map(constraintViolation -> {
                            final String msg = constraintViolation.getMessage();
                            final String[] tokens = constraintViolation.getPropertyPath().toString().split("\\.");
                            return tokens[tokens.length - 1] + ": " + msg;
                        })
                        .orElse(ex.getMessage());
        return buildResponseError(HttpStatus.BAD_REQUEST, message, ErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(MethodArgumentTypeMismatchException ex) {
        log.error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex);
        final String message = "Parameter '" + ex.getName() + "' must be of type " +
                ex.getParameter().getParameterType().getSimpleName();
        return buildResponseError(HttpStatus.BAD_REQUEST, message, ErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(MethodArgumentNotValidException ex) {
        log.error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex);
        final String message =
                Optional.ofNullable(ex.getFieldError())
                        .map(fieldError ->
                                "Field '" + fieldError.getField() + "' invalid: " + fieldError.getDefaultMessage())
                        .orElse("Error");
        return buildResponseError(HttpStatus.BAD_REQUEST, message, ErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(HttpMessageNotReadableException ex) {
        log.error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex);
        final ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        return buildResponseError(HttpStatus.BAD_REQUEST, errorCode.getReasonPhrase(), errorCode);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(MissingServletRequestParameterException ex) {
        final String message =
                "Parameter '" + ex.getParameterName() + "' of type " + ex.getParameterType() + " is required";
        return buildResponseError(HttpStatus.BAD_REQUEST, message, ErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex) {
        log.error(HttpStatus.NOT_FOUND.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.NOT_FOUND, ex, ex.getCode());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleUnprocessableEntity(BusinessException ex) {
        log.error(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.UNPROCESSABLE_ENTITY, ex, ex.getCode());
    }

    private ResponseEntity<ApiErrorResponse> buildResponseError(
            final HttpStatus httpStatus,
            final Throwable ex,
            final ErrorCode errorCode
    ) {
        return buildResponseError(httpStatus, ex.getMessage(), errorCode);
    }

    private ResponseEntity<ApiErrorResponse> buildResponseError(
            final HttpStatus httpStatus,
            final String message,
            final ErrorCode errorCode
    ) {
        final ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .timestamp(ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC))
                .name(httpStatus.getReasonPhrase())
                .description(message)
                .status(httpStatus.value())
                .code(String.valueOf(errorCode.value()))
                .resource(httpServletRequest.getRequestURI())
                .build();

        return new ResponseEntity<>(apiErrorResponse, httpStatus);
    }

    @Builder
    @NonNull
    @lombok.Value
    public static class ApiErrorResponse {
        private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss[.SSS]X";

        String name;
        Integer status;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
        ZonedDateTime timestamp;
        String code;
        String resource;
        String description;
    }

}
