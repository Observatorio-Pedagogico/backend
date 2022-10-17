package com.obervatorio_pedagogico.backend.presentation.dto.usuario.response;

import com.obervatorio_pedagogico.backend.presentation.model.usuario.EnvelopeFuncionario.TipoFuncionario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnvelopeFuncionarioResponse {
    private FuncionarioResponse funcionario;

    private TipoFuncionario tipoFuncionario;
}
