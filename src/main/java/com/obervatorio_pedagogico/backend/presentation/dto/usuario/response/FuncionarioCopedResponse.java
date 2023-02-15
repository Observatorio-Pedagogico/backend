package com.obervatorio_pedagogico.backend.presentation.dto.usuario.response;

import com.obervatorio_pedagogico.backend.domain.model.usuario.FuncionarioCoped.TipoCargo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FuncionarioCopedResponse extends UsuarioResponse {

    private TipoCargo cargo;
}
