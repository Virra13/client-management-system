package com.backEndSpring.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDescription {
    private String errorMessage;
}
