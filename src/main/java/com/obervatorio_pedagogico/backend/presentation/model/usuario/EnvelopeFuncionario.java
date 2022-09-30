package com.obervatorio_pedagogico.backend.presentation.model.usuario;

import com.obervatorio_pedagogico.backend.domain.model.usuario.Funcionario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnvelopeFuncionario {
    private Funcionario funcionario;
    private TipoFuncionario tipoFuncionario;

    public Boolean isFuncionarioCoped() {
        return tipoFuncionario.equals(TipoFuncionario.FUNCIONARIO_COPED);
    }

    public Boolean isProfessor() {
        return tipoFuncionario.equals(TipoFuncionario.PROFESSOR);
    }

    public enum TipoFuncionario {
        FUNCIONARIO_COPED, PROFESSOR
    }
}
