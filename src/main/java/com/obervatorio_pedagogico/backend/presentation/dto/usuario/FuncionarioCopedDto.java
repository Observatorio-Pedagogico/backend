package com.obervatorio_pedagogico.backend.presentation.dto.usuario;

import com.obervatorio_pedagogico.backend.domain.model.usuario.Coped.TipoCargo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FuncionarioCopedDto extends UsuarioDto {

    private TipoCargo cargo;
}
