package com.obervatorio_pedagogico.backend.presentation.dto.auth.request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Valid
    @NotEmpty
    @NotBlank
    private String email;
    
    @Valid
    @Size(min = 6, message = "a senha deve conter pelo menos 6 caracteres")
    private String senha;
}
