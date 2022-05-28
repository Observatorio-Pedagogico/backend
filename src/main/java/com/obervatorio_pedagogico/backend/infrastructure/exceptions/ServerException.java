package com.obervatorio_pedagogico.backend.infrastructure.exceptions;

public class ServerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MSG = "Erro no servidor.";

    public ServerException(String message) {
        super(message != null ? message : DEFAULT_MSG);
    }

    public ServerException() {
        super(DEFAULT_MSG);
    }
}
