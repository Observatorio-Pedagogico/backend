package com.obervatorio_pedagogico.backend.domain.exceptions;

import com.obervatorio_pedagogico.backend.infrastructure.exceptions.annotation.BusinessException;

import org.springframework.http.HttpStatus;

@BusinessException(
        key = "usuario.nao-encontrado",
        status = HttpStatus.NOT_FOUND,
        returnMessageException = true
)
public class UsuarioNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsuarioNaoEncontradoException(AtributoBuscado atributoBuscado, String tipo, String atributoBuscaValor) {
        super(criarMensagem(atributoBuscado, tipo, atributoBuscaValor));
    }

    private static String criarMensagem(AtributoBuscado atributoBuscado, String tipo, String atributoBuscaValor) {
        return String.format("%s pelo atributo %s=%s não encontrado", tipo, atributoBuscado.toString(), atributoBuscaValor);
    }

    public enum AtributoBuscado {
        MATRICULA, NOME;
    }
}
