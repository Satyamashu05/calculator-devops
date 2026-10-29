package com.spe.calculator;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissing(MissingServletRequestParameterException ex,
                                                       HttpServletRequest req) {
        return badRequest("Missing required parameter '" + ex.getParameterName() + "'", req);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                            HttpServletRequest req) {
        return badRequest("Parameter '" + ex.getName() + "' must be a valid number", req);
    }

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<ErrorResponse> handleArithmetic(ArithmeticException ex, HttpServletRequest req) {
        return badRequest(ex.getMessage(), req);
    }

    private ResponseEntity<ErrorResponse> badRequest(String message, HttpServletRequest req) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                new ErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), message, req.getRequestURI()));
    }
}
