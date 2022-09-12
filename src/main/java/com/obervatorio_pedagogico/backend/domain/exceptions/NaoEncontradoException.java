package com.obervatorio_pedagogico.backend.domain.exceptions;

import org.springframework.http.HttpStatus;

import com.obervatorio_pedagogico.backend.infrastructure.exceptions.annotation.BusinessException;

@BusinessException(
    key = "nao-encontrado",
    status = HttpStatus.NOT_FOUND,
    returnMessageException = true
)
public class NaoEncontradoException extends RuntimeException {
    
    private static final String MENSAGEM = "";

    public NaoEncontradoException() {
        super(MENSAGEM);
    }

    public NaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
