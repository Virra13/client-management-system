package com.clientmanagement.backend.controller.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@RestControllerAdvice(annotations = RestController.class)
public class RestControllerException {

    @ExceptionHandler(exception = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> error(ConstraintViolationException exception) {
        ValidationErrorResponse errorResponse =
                new ValidationErrorResponse(
                        "Произошла ошибка валидации",
                        buildConstraintViolationException(exception.getConstraintViolations())
                );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> error(MethodArgumentNotValidException exception) {
        ValidationErrorResponse errorResponse =
                new ValidationErrorResponse(
                        "Произошла ошибка валидации",
                        buildMethodArgumentException(exception.getBindingResult())
                );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(exception = {Exception.class})
    public ResponseEntity<ErrorResponse> error(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(exception = {NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> error(NoSuchElementException exception) {
        String message =
                Optional.ofNullable(exception.getMessage())
                        .orElse("Запрашиваемый ресурс не найден");

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(message));
    }

    private List<ErrorDescription> buildConstraintViolationException(Set<ConstraintViolation<?>> violations) {
        return violations
                .stream()
                .map(res -> new ErrorDescription(res.getMessage()))
                .toList();
    }

    private List<ErrorDescription> buildMethodArgumentException(BindingResult bindingResult) {
        return bindingResult
                .getFieldErrors()
                .stream()
                .map(res -> new ErrorDescription(res.getDefaultMessage()))
                .toList();
    }
}