package com.obervatorio_pedagogico.backend.domain.exceptions;

import com.obervatorio_pedagogico.backend.infrastructure.exceptions.annotation.BusinessException;

import org.springframework.http.HttpStatus;

@BusinessException(
        key = "login.usuario.nao-permitido",
        status = HttpStatus.FORBIDDEN,
        returnMessageException = true
)
public class UsuarioNaoPermitidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String MENSAGEM = "";

    public UsuarioNaoPermitidoException() {
        super(MENSAGEM);
    }
}
