package com.obervatorio_pedagogico.backend.presentation.dto.extracao.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExtracaoThreadResponse {

    private Integer porcentagemEnvio;

    private ExtracaoResponseResumido extracao;
}
