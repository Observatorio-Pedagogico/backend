package com.obervatorio_pedagogico.backend.presentation.dto.usuario.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AlunoResumidoResponse extends UsuarioResponse {
    private BigDecimal cre;
}
