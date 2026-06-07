package com.uiSpring;

import lombok.Getter;
import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    private final List<String> errorMessages;

    public ValidationException(String message, List<String> errorMessages) {
        super(message);
        this.errorMessages = errorMessages;
    }
}
