package com.obervatorio_pedagogico.backend.presentation.dto.usuario.response;

import com.obervatorio_pedagogico.backend.domain.model.usuario.Usuario.Sexo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FuncionarioResponse {

    private String matricula;

    private String email;

    private String nome;

    private Sexo sexo;
}