package com.obervatorio_pedagogico.backend.domain.exceptions;

public class ErroConversaoArquivoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String MENSAGEM = "erro ao converter o arquivo";

    public ErroConversaoArquivoException() {
        super(MENSAGEM);
    }

}
