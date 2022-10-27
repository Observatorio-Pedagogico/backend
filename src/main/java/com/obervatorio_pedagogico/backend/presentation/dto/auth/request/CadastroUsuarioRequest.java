package com.obervatorio_pedagogico.backend.presentation.dto.auth.request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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

    @Valid
    @NotEmpty
    @NotBlank
    @Size(min = 6, message = "a matricula deve conter pelo menos 6 caracteres")
    private String matricula;

    @Valid
    @NotEmpty
    @NotBlank
    private String email;

    @Valid
    @Size(min = 6, message = "a senha deve conter pelo menos 6 caracteres")
    private String senha;

    @Valid
    @NotEmpty
    @NotBlank
    private String nome;

    private Sexo sexo;

    private Tipo tipo;

    public enum Tipo {
        COPED, PROFESSOR
    }
}
