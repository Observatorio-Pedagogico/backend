package com.obervatorio_pedagogico.backend.domain.exceptions;

import com.obervatorio_pedagogico.backend.infrastructure.exceptions.annotation.BusinessException;

import org.springframework.http.HttpStatus;

@BusinessException(
        key = "arquivo.formato-nao-suportado",
        status = HttpStatus.BAD_REQUEST,
        returnMessageException = true
)
public class FormatoArquivoNaoSuportadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FormatoArquivoNaoSuportadoException(String formato) {
        super(criarMensagem(formato));
    }

    private static String criarMensagem(String formato) {
        return String.format("%s nao suportado", formato);
    }
}
