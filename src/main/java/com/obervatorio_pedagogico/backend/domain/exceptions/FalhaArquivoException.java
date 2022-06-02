package com.obervatorio_pedagogico.backend.domain.exceptions;

import com.obervatorio_pedagogico.backend.infrastructure.exceptions.annotation.BusinessException;

import org.springframework.http.HttpStatus;

@BusinessException(
        key = "arquivo.formato-nao-suportado",
        status = HttpStatus.BAD_REQUEST,
        returnMessageException = true
)
public class FalhaArquivoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String MENSAGEM = "Falha ao converter o arquivo";

    public FalhaArquivoException() {
        super(MENSAGEM);
    }
}
