package com.obervatorio_pedagogico.backend.infrastructure.exceptions;

public class RequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG = "Requisição inválida.";

    public RequestException(String message) {
        super(message != null ? message : DEFAULT_MSG);
    }

    public RequestException() {
        super(DEFAULT_MSG);
    }
}
