package com.obervatorio_pedagogico.backend.presentation.dto.extracao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExtracaoThreadResponse {

    private Integer porcentagemDeEnvio;

    private ExtracaoResponseResumido extracao;
}
