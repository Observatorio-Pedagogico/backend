package com.obervatorio_pedagogico.backend.domain.exceptions;

import com.obervatorio_pedagogico.backend.infrastructure.exceptions.annotation.BusinessException;

import org.springframework.http.HttpStatus;

@BusinessException(
        key = "disciplina.nao-encontrado",
        status = HttpStatus.NOT_FOUND,
        returnMessageException = true
)
public class DisciplinaNaoEncontradaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DisciplinaNaoEncontradaException(AtributoBuscado atributoBuscado, String atributoBuscaValor) {
        super(criarMensagem(atributoBuscado, atributoBuscaValor));
    }

    private static String criarMensagem(AtributoBuscado atributoBuscado, String atributoBuscaValor) {
        return String.format("%s pelo atributo %s=%s não encontrado", atributoBuscado.toString(), atributoBuscaValor);
    }

    public enum AtributoBuscado {
        CODIGO, NOME;
    }
}
