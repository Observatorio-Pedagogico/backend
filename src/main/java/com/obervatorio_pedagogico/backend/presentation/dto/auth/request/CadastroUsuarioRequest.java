package com.obervatorio_pedagogico.backend.presentation.dto.auth.request;

import com.obervatorio_pedagogico.backend.domain.model.usuario.Usuario.Sexo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CadastroUsuarioRequest {

    private String matricula;

    private String email;

    private String senha;

    private String nome;

    private Sexo sexo;

    private Tipo tipo;

    public enum Tipo {
        COPED, PROFESSOR
    }
}
