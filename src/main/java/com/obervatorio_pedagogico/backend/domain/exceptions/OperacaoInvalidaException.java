package com.obervatorio_pedagogico.backend.domain.exceptions;

import com.obervatorio_pedagogico.backend.infrastructure.exceptions.annotation.BusinessException;

import org.springframework.http.HttpStatus;

@BusinessException(
        key = "operacao.operacao-invalida",
        status = HttpStatus.BAD_REQUEST,
        returnMessageException = true
)
public class OperacaoInvalidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OperacaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
