package com.uiSpring.rest_client;

import org.springframework.http.HttpStatusCode;

public class ExternalApiException extends RuntimeException {
    private final HttpStatusCode statusCode;
    private final String responseBody;

    public ExternalApiException(String message, HttpStatusCode statusCode, String responseBody) {
        this(message, statusCode, responseBody, null);
    }

    public ExternalApiException(String message, HttpStatusCode statusCode, String responseBody, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public HttpStatusCode getStatusCode() { return statusCode; }
    public String getResponseBody() { return responseBody; }
}
