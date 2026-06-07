package com.backEndSpring.controller.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidationErrorResponse extends ErrorResponse {

    private List<ErrorDescription> errorDescriptions;

    public ValidationErrorResponse(String message, List<ErrorDescription> errorDescriptions) {
        super(message);
        this.errorDescriptions = errorDescriptions;
    }
}
