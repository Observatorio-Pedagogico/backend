package com.obervatorio_pedagogico.backend.domain.exceptions;

import com.obervatorio_pedagogico.backend.infrastructure.exceptions.annotation.BusinessException;

import org.springframework.http.HttpStatus;

@BusinessException(
        key = "login.usuario.ja-existente",
        status = HttpStatus.BAD_REQUEST,
        returnMessageException = true
)
public class UsuarioJaExistenteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String MENSAGEM = "";

    public UsuarioJaExistenteException() {
        super(MENSAGEM);
    }
}
